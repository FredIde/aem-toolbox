# Tricode AEM toolbox
A collection of Adobe Experience Manager related tools that are useful in most projects.
Offers a plugin mechanism that allows one to create custom plugins.

## Requirements

Currently CQ 5.6.x or AEM 6 are supported.

## Current functionality:

* **Package size estimation**

Allows one to estimate the size that a package will be when its built. Comes in handy when you want to create content packages but are not sure how big the included content is.
Its only possible to select packages that have not been built before or where the filter has been changed, otherwise this would not make sense since the size is already known.

* **Data migration**

A data migration mechanism for repository content. Can be used for example to migrate component data structures when a new version of the component is created that stores is data differently
and existing component instances may not be broken. To add a custom patch create an OSGI service that implements and exposes itself using the *nl.tricode.aem.tools.plugins.datamigration.Patch* interface.

## Build instructions

To build the project you have to have the repo.adobe.com repository (or a mirror of it) in your Maven configuration.

Do a `mvn clean package` to build the CRX package. It will be located in the content/target folder.

Its also possible to deploy to your local instance after build, for that activate the *installPackage* profile: `mvn clean install -PinstallPackage`

## Support and warranty

Use at your own risk, the code comes without any warranty. In case you need support for this or code or custom plugins we can offer consultancy services. See [http://www.tricode.nl/](http://www.tricode.nl/en/tricode-nl.html) for more information.