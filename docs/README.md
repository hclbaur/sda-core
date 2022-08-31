# Rationale V2.0

I think it was somewhere in 2021, when I was working on SDS and implementing the 
schema parser/validator in Java, that I got this nagging feeling that something 
was not entirely right about SDA. While the syntax lived up to the promise of 
being simple, I could not wholeheartedly say the same about the use of the Java 
library I wrote to programatically support it.

Although not overly complicated, writing code to work with SDA nodes required a 
fair amount of casting, due to the fact that SDA nodes were represented by two 
distinct classes, each subclassing an abstract Node class.

There was one class to support what I called "a simple node", that could have a 
(string) value, and another one for "complex nodes", which could contain other 
nodes (simple or complex ones).

At the time it seemed like this was the "proper" way to implement it, because I 
felt that - conceptually - these were entirely different entities that warranted 
different classes with different methods. And yet, there was no denying that it 
was also a bit of a hassle.

I kept going back and forth about it, trying to find a way to both have my cake 
and eat it too, until it hit me that maybe the SDA syntax was to uptight about
nodes .... (to be continued) 

Maybe it was a "cleaner" or "better" design, I really do not 




 
**Harold C.L. Baur, September 2022**


# Rationale V1.0

SDA was dreamed up when, one day, I was contemplating how much I disliked XML when 
it comes to exchanging data in integration projects. Although the merits of XML 
(and XSD, and XSLT) are unquestionable, I could not help thinking that

- Some of its features are unnecessarily complicated.
- It is inefficient with regards to use of storage and processing resources.
- For a – supposedly - human readable format, it is surprisingly unpleasant to 
the eye (this is probably a matter of personal taste).

I know, beauty is in the eye of the beholder. And qualifications like “complicated” 
say as much about me as they say about XML. And since computers get more powerful 
all time, one might consider use of resources a non-issue all together.

But even if resources are abundant, it does not mean we should not care what they 
are used for. And if a lot of smart people spent a lot of time debating the 
appropriate use of a certain feature, then maybe it is too complicated. Sometimes, 
XML just seems overkill[^1].

[^1]: when this document was nearly finished, I Googled for “alternatives for XML” 
and learned several things. First, that I should check Google before doing anything 
else. Second, that I am not alone (google for “xml sucks”). And third, that for 
data exchange there is at least one well-known alternative, called JSON.

In any case, that morning, SDA became my pet project. It obviously was not going to 
replace XML ever, but I was curious how far I could take it, and whether it could 
be both powerful and steer clear of complexity at the same time. And if nothing 
else, toying around with this concept would increase my understanding (and possibly 
appreciation) of XML. And lastly, I needed an excuse to brush up on my Java 
programming.

SDA means Structured Data, or more affectionately, Simple Data.

**Harold C.L. Baur, November 2008**

---
