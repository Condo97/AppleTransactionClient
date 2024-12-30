# AppleTransactionClient

**AppleTransactionClient** is a Java library designed to interact with Apple's App Store subscription APIs. It allows you to generate JSON Web Tokens (JWT) for authentication and retrieve subscription status responses seamlessly.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
  - [Generating JWT](#generating-jwt)
  - [Retrieving Subscription Status](#retrieving-subscription-status)
- [Examples](#examples)
- [Testing](#testing)
- [Publishing](#publishing)
- [Contributing](#contributing)
- [License](#license)

## Features

- **JWT Generation:** Easily create JWTs required for authenticating with Apple’s App Store APIs.
- **Subscription Status Retrieval:** Fetch and parse subscription status from Apple’s servers.
- **Error Handling:** Comprehensive error handling tailored for Apple App Store responses.
- **Modular Design:** Separation of API and implementation using Gradle’s `java-library` plugin.
- **Support for Production and Sandbox Environments:** Automatically switch between production and sandbox URLs based on response.

## Prerequisites

- **Java:** JDK 17 or higher
- **Gradle:** Version 8.2.1 or compatible
- **Dependencies:**
  - [Jackson](https://github.com/FasterXML/jackson) for JSON processing
  - [Java JWT](https://github.com/auth0/java-jwt) for JWT handling
  - [Guava](https://github.com/google/guava) for utility functions
  - [Httpson](https://github.com/condo97/Httpson) for HTTPS handling

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/condo97/AppleTransactionClient.git
   cd AppleTransactionClient
   ```

2. **Build the Project:**

   ```bash
   ./gradlew build
   ```

3. **Publish to Local Maven Repository (Optional):**

   ```bash
   ./gradlew publishToMavenLocal
   ```

## Configuration

1. **Update `build.gradle`:**

   Ensure all dependencies are correctly specified. The provided `build.gradle` is pre-configured with necessary dependencies and plugins.

2. **Set Up JWT Signing:**

   - Place your private key (`.p8` file) in a secure location.
   - Note the `privateKeyID` associated with your Apple Developer account.
   - Configure the paths and IDs in your application as required.

## Usage

### Generating JWT

To interact with Apple’s App Store APIs, you need to generate a JWT. Use the `JWTSigner` class for this purpose.

```java
import appletransactionclient.JWTSigner;

// Initialize JWTSigner with the path to your private key and your key ID
JWTSigner signer = new JWTSigner("path/to/AuthKey.p8", "YOUR_KEY_ID");

// Generate JWT with required payload
String jwt = signer.signJWT(Map.of(
    "iss", "YOUR_ISSUER_ID",
    "iat", System.currentTimeMillis() / 1000L,
    "exp", (System.currentTimeMillis() / 1000L) + 80,
    "aud", "appstoreconnect-v1",
    "bid", "com.your.bundleid"
));
```

### Retrieving Subscription Status

Once you have the JWT, you can retrieve the subscription status using `SubscriptionAppleHttpClient`.

```java
import appletransactionclient.SubscriptionAppleHttpClient;
import appletransactionclient.exception.AppStoreErrorResponseException;
import appletransactionclient.http.response.status.AppStoreStatusResponse;

// Initialize the client with base URLs and path
SubscriptionAppleHttpClient client = new SubscriptionAppleHttpClient(
    "https://api.storekit.itunes.apple.com/",
    "https://api.storekit-sandbox.itunes.apple.com/",
    "/inApps/subscriptions/"
);

// Fetch subscription status
try {
    AppStoreStatusResponse response = client.getStatusResponseV1(transactionID, jwt);
    // Process the response
} catch (AppStoreErrorResponseException | IOException | InterruptedException | URISyntaxException e) {
    e.printStackTrace();
    // Handle exceptions
}
```

## Examples

### Complete Workflow Example

```java
import appletransactionclient.JWTSigner;
import appletransactionclient.SubscriptionAppleHttpClient;
import appletransactionclient.http.response.status.AppStoreStatusResponse;
import appletransactionclient.exception.AppStoreErrorResponseException;

import java.util.Map;

public class ExampleUsage {
    public static void main(String[] args) {
        try {
            // Initialize JWTSigner
            JWTSigner signer = new JWTSigner("path/to/AuthKey.p8", "YOUR_KEY_ID");

            // Generate JWT
            String jwt = signer.signJWT(Map.of(
                "iss", "YOUR_ISSUER_ID",
                "iat", System.currentTimeMillis() / 1000L,
                "exp", (System.currentTimeMillis() / 1000L) + 80,
                "aud", "appstoreconnect-v1",
                "bid", "com.your.bundleid"
            ));

            // Initialize HTTP Client
            SubscriptionAppleHttpClient client = new SubscriptionAppleHttpClient(
                "https://api.storekit.itunes.apple.com/",
                "https://api.storekit-sandbox.itunes.apple.com/",
                "/inApps/subscriptions/"
            );

            // Retrieve subscription status
            Long transactionID = 1234567890L;
            AppStoreStatusResponse response = client.getStatusResponseV1(transactionID, jwt);

            // Handle the response
            System.out.println("Environment: " + response.getEnvironment());
            System.out.println("Bundle ID: " + response.getBundleId());
            // Further processing...

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }
}
```

## Testing

The project includes unit tests using JUnit Jupiter. To run the tests:

```bash
./gradlew test
```

Test classes are located in the `src/test/java/appletransactionclient` directory.

## Publishing

This library uses Gradle's `maven-publish` plugin. To publish the library to a Maven repository:

1. **Configure Publishing Details in `build.gradle`:**

   Ensure the `publishing` section is correctly set up with your repository details.

2. **Publish the Library:**

   ```bash
   ./gradlew publish
   ```

   For publishing to a local Maven repository:

   ```bash
   ./gradlew publishToMavenLocal
   ```

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the Repository**
2. **Create a Feature Branch:**

   ```bash
   git checkout -b feature/YourFeature
   ```

3. **Commit Your Changes**
4. **Push to the Branch:**

   ```bash
   git push origin feature/YourFeature
   ```

5. **Create a Pull Request**

Please ensure your code adheres to the project's coding standards and all tests pass.

## License

This project is licensed under the [MIT License](LICENSE).
