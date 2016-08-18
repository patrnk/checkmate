# Check mate
Cross-platform desktop application that allows automating test grading.

Note: the interface is fully in Russian. If you want a localized version, contact maintainer.

## The application can
  - grade provided answers based on test's answer key and grading algorithm
  - store the grades and provided answers
  - retrieve the answers from Yandex Mail

You can extend the app by adding mail providers and grading algorithms.

## In order to add a grading algorithm, you need to
  1. Create a class implementing Test interface. Don't forget about SerializationProxy! Use SimpleTest class as an example.
  2. Create a class implementing TestFactory interface. This class will be providing intances of your Test class.
  3. Add your TestFactory to getFactories() method of CreateTestSceneController class.

The app was built using JavaFX and NetBeans 8.1.
