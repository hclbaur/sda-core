# SDA Core

The SDA project was conceived in 2008 and aims to produce Java libraries that (ultimately) support parsing, formatting, processing, validation and transformation of SDA content. The SDA core library supplies the classes that make up the basic building blocks of SDA (nodes) as well as a parser and formatter.

## What is SDA

SDA (Simple DAta) is a way of representing structured data in a fashion that pleases both humans and machines. Unlike XML, it is *just* a data format, and much less powerful. On the other hand, it is faster to process, simpler to learn, and easier on the eye. It's more like JSON, but with fewer punctuation and weakly typed.

For example:

	addressbook {
		contact "1" {
			firstname "Alice"
			phonenumber "06-11111111"
			phonenumber "06-22222222"
		}
		contact "2" {
			firstname "Bob"
			phonenumber "06-33333333"
			phonenumber "06-44444444"
		}
	}

As you can see the format borders on self-explanatory. But for completeness' sake, I wrote a [tutorial](docs/).

## Running the demo

To run the demo, get `demo.jar`, `addressbook.sda` and `badbook.sda` from  the latest [release](https://github.com/hclbaur/sda-core/releases/latest) and copy these to a temporary directory where you will run the demo. Assuming the java executable is in your path, run it like this:

	java -jar demo.jar addressbook.sda
	
which will output the following

	Alice has 2 phone number(s).
	  Number 1: 06-11111111
	  Number 2: 06-22222222
	Bob has 2 phone number(s).
	  Number 1: 06-33333333
	  Number 2: 06-44444444

When you look at the [code](src/test/java/demo.java) you will see that parsing the input file takes a single line of code, after which you can process the nodes.

However, there is a catch. The parser does not *validate* the input; it only checks that it is *well-formed*. Basically, if all the curly braces properly match, all values are quoted, and the node names have no funny characters, the parser doesn't really care what the data represents. 

Therefor, it's easy to upset the demo if you feed it something that can be parsed without problems, but fails to meet its expectations, such as

	addressbook {
		contact "1" {
			firstname "Alice"
			phonynumber "06-11111111"
			phonenumber "06-22222222"
		}
		contact "2" {
			phonenumber "06-33333333"
			phonenumber "06-44444444"
		}
	}

No need to type that in, just run:

	java -jar demo.jar badbook.sda
	
and it will output

	Alice has 1 phone number(s).
	  Number 1: 06-22222222
	Exception in thread "main" java.lang.NullPointerException
			at demo.main(demo.java:23)
	
Oops. The demo expects all contacts to be properly named, but as it happens, the second one is anonymous. And the programmer (that would me) failed to write more defensive code!

It's easy to fix this issue by adding a check on a null reference, but the point, really, is that defensive code goes only so far. Have a closer look at the output. Alice seems to have only one number, and it's actually the second number that gets printed there.

This is because there is a more subtle (and potentially even more dangerous) issue. The node carrying the first number is misspelled "phonynumber", which the demo does not look for, and the parser does not care about.   

It is easy to write code that checks for the absence of something you know you need, but more cumbersome to check for the presence of something you do not expect, in particular when your data structure becomes larger than this trivial example.

Enter *schema* validation. A schema is a formal description of what the data should look like, so the parser can check for missing, incorrect or unknown data *before* it is processed and causes a problem.

It is possible to write such a schema for SDA content. But that is another story, and in fact, another [project](https://github.com/hclbaur/sds-core) :)

----