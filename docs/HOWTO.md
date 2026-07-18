# SDA HOWTO

This guide explains how to use the SDA core library, which facilitates working with hierarchical data structures through the `DataNode` class and the `SDA` utility class.

---

## 1. Creating Nodes

The `DataNode` is the fundamental building block of SDA documents. Each node has a name (or tag) and can optionally contain a value and/or child nodes.

```
DataNode tag = new DataNode("tag");  // Node with an empty string value
DataNode name = new DataNode("name", "John Doe");  // Node with a value
```

Which creates these nodes:

```
tag ""
name "John Doe"
```

Node names in SDA must follow specific rules - refer to the [tutorial](TUTORIAL.md) or [specification](SPECIFICATION.md) for details. You can validate a node name using the `isNodeName` method:

```
if (SDA.isNodeName(someName)) {
    DataNode node = new DataNode(someName, someValue);
}
```

---

## 2. Building Hierarchies (Parent Nodes)

SDA allows nodes to contain other nodes, creating a tree structure:

```
DataNode person = new DataNode("person");
person.add(new DataNode("firstName", "John"));
person.add(new DataNode("lastName", "Kennedy"));
person.add(1, new DataNode("middleName", "F."));
```

The last statement adds a node at index 1 rather than at the end, so the result will be:

```
person {
    firstName "John"
	middleName "F."
    lastName "Kennedy"
}
```

If you need a node with an empty child list, use `expand()`:

```
DataNode folder = new DataNode("folder");
folder.expand();
```

This represents: `folder { }`, and this is neither a leaf nor a parent node (see section 4).

Unlike many data formats, SDA uniquely allows a node to have **both** a value and child nodes:

```
DataNode person = new DataNode("person", "John");
person.add(new DataNode("lastName", "Doe"));
```

which represents: `person "John" { lastName "Doe" }`

---

## 3. Managing Values and Content

All values in SDA are treated as strings; the library does not enforce type constraints.

```
DataNode node = new DataNode("example", "initial value");
String currentValue = node.getValue();  // Returns "initial value"
node.setValue("new value");
```

The library automatically converts `null` to an empty string.

```
DataNode tag = new DataNode("tag", null);  // Value becomes ""
String val = tag.getValue();  // Returns empty string, not null
```

When serializing values that contain quotes or backslashes, the library automatically escapes them:

```
DataNode quote = new DataNode("text", "He said \"hello\"");
quote.toString();  // Returns: text "He said \"hello\""
quote.getValue();  // Returns: He said "hello"
```

---

## 4. Checking Node Type and Navigation

```
DataNode leaf = new DataNode("leafnode", "value");
DataNode parent = new DataNode("parentnode"); parent.add(leaf);
DataNode empty = new DataNode("emptynode"); empty.expand();

leaf.isLeaf();    // true
parent.isLeaf();  // false
empty.isLeaf();   // false!

leaf.isParent();    // false
parent.isParent();  // true
empty.isParent();   // false!
```

> **Note:** A node with an empty child list (created via `expand()`) is neither a parent, nor a leaf.

### Accessing Child Nodes

```
DataNode parent = new DataNode("contacts");
parent.add(new DataNode("name", "Alice"));
parent.add(new DataNode("phone", "555-1234");

// Get all child nodes
List<DataNode> children = parent.nodes();  // Unmodifiable list [name, phone]

// Get first child with a specific name
DataNode name = parent.get("name");  // Returns the node or null

// Get all children with a specific name
List<DataNode> phones = parent.getAll("phone");
```

### Finding Descendant Nodes (Deep Search)

Use `find()` to search the entire subtree:

```
DataNode root = new DataNode("root");
// ... populate with nested structure ...

// Find all descendants matching a predicate
List<DataNode> results = root.find(n -> n.getName().contains("id"));
```

### Getting the Path to a Node

```
DataNode addressbook = new DataNode("addressbook");
DataNode contact = new DataNode("contact", "1");
DataNode name = new DataNode("name", "Alice");
contact.add(name);
addressbook.add(contact);

String path = name.path();  // Returns "/addressbook/contact/name"
```

---

## 5. Cloning Nodes

Create a complete, independent copy of a node and all its descendants:

```
DataNode original = new DataNode("data", "value");
original.add(new DataNode("child", "content"));

DataNode backup = original.copy();  // Deep copy
```

Modifications to `backup` will not affect `original`.

---

## 6. Parsing SDA Content

### Using Convenience Methods

The `SDA` class provides convenient static methods for parsing:

**Parse from a String:**
```
String input = "greeting { message \"hello world\" }";
DataNode root = SDA.parse(input);
```

**Parse from a File:**
```
import java.io.File;

File file = new File("data.sda");
DataNode root = SDA.parse(file);
```

**Parse from a Reader:**
```
import java.io.Reader;
import java.io.StringReader;

Reader reader = new StringReader("node \"value\"");
DataNode root = SDA.parse(reader);
```

### Exception Handling

Parsing can throw exceptions if the input is malformed:
```
try {
    DataNode root = SDA.parse(new File("data.sda"));
} catch (java.io.IOException e) {
    System.err.println("I/O error: " + e.getMessage());
} catch (be.baur.sda.io.ParseException e) {
    System.err.println("Syntax error at position " + e.getErrorOffset());
}
```

---

## 7. Formatting and Serializing SDA

The following code

```
DataNode node = new DataNode("root", "value");
node.add(new DataNode("child", "data"));

String sda = SDA.format(node);
System.out.println(sda);
```

will output (tab indented)

```
root "value" {
	child "data"
}
```

Or write it to a file:

```
import java.io.File;

SDA.format(new File("output.sda"), node);
```

Formatting options (custom indentation):

```
import be.baur.sda.io.SDAFormatter;
import java.io.StringWriter;

// Create formatter with 4-space indentation (default uses tabs)
SDAFormatter formatter = new SDAFormatter(4);

StringWriter output = new StringWriter();
formatter.format(output, node);
String result = output.toString();
```

---

## 8. Practical Example: Address Book

Here's an example demonstrating the key concepts:
```
import be.baur.sda.DataNode;
import be.baur.sda.SDA;

import java.io.File;
import java.util.List;

public class AddressBookExample {

    public static void main(String[] args) throws Exception {
        
        // Parse an existing SDA file
        File file = new File("addressbook.sda");
        DataNode addressbook = SDA.parse(file);
        
        // Summarize contacts
        List<DataNode> contacts = addressbook.getAll("contact");
        if (! contacts.isEmpty()) {
            
            for (DataNode contact : contacts) {
                DataNode firstname = contact.get("firstname");
                var numbers = contact.getAll("phonenumber");
                
                System.out.println(firstname.getValue() + 
                		" has " + numbers.size() + " phone number(s).");
            }
        }
        
        // Add new contact
        DataNode newContact = new DataNode("contact", ""+(contacts.size()+1));
        newContact.add(new DataNode("firstname", "Charlie"));
        newContact.add(new DataNode("phonenumber", "555-5555"));
        addressbook.add(newContact);
        
        // Write back to file
        SDA.format(new File("addressbook_updated.sda"), addressbook);
    }
}
```

Given an `addressbook.sda` file like

```
addressbook {
    contact "1" {
        firstname "Alice"
        phonenumber "06-11111111"
        phonenumber "06-22222222"
    }
    contact "2" {
        firstname "Bob"
        phonenumber "06-33333333"
    }
}
```

the program would output

```
Alice has 2 phone number(s).
Bob has 1 phone number(s).
```

and add a new contact "Charlie" at the end.

---

## 9. When to Use the SDA Library

The SDA library is ideal for:
- **Configuration files** – Simple, human-readable hierarchical data
- **Data interchange** – Lightweight alternative to XML or JSON for specific use cases
- **Structured logging** – Organizing multi-level log entries
- **Tree-based data** – Applications that work with hierarchical information

For more complex validation and schema support, consider using the related SDS (SDA Schema) project.
