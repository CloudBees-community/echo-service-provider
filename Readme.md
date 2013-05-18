# CloudBees Sample Service Provider

A CloudBees ecosystem provider is a web app that implements set of REST/Http based APIs that speak JSON.

Steps included

* Define a manifest.

See manifest.json. It has no billing information defined. Billing info is needed if your provider
offers at least one paid service. Please contact CloudBees if you offer a paid service, which typically is the case.
It's not self service yet.

* Decide whether you offer a Subscription only service or a Resource based service

CloudBees add on mechanism offers you to package your service either as Subscription only or a Subscription and
Resource based. Subscription only service makes sense for things that are offered at organization account level.
For example email service provider.

Where as Resource based scheme would mean you want to offer your service such that it can be subscribed multiple times by an
account holder, for example mysql database, you can create different mysql db for different apps running inside cloudbees.

Please note that Subscription and Resource has 1 to many relation.

* Implement your service provider.

For details see the API doc <a href="https://developer.cloudbees.com/bin/view/RUN/Service+Provider+API"> here</a>.