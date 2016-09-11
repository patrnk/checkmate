# Check mate
Note: the interface is fully in Russian. If you need a localized version, contact the maintainer.

The guide on how to use CheckMate can be found at http://91.ru/checkmate/ (it's also in Russian).

## The application can
  - grade provided answers based on test's answer key and grading algorithm
  - store the grades and provided answers
  - retrieve the answers from Yandex Mail

## Contribute
You can extend the app by adding mail providers (see Mailbox class) and grading algorithms (see below).

## In order to add a grading algorithm, you need to
  1. Create a class implementing Test interface. Don't forget about SerializationProxy! Use SimpleTest class as an example.
  2. Create a class implementing TestFactory interface. This class will be providing intances of your Test class.
  3. Add your TestFactory to getFactories() method of CreateTestSceneController class.

The app was built with JavaFX and NetBeans 8.1.
