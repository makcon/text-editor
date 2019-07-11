# Implementation of text editor service

## How to build the service

### Using an IDE

1. Clone or download the repository
2. Open the project in your favorite IDE (e.q. Intellij Idea)
3. The service does not use an external database. It's based on files. By default the service will write texts under default `texts` directory located in the root of the project. If you want to change it then define the jvm arg: `-Dtexts.folder=PATH_TO_YOUR_FOLDER` 
4. Run spring-boot `test.maksim.editor.ws.Application` class of `text-editor-ws` module. The service will be running on port `8080`

### Using command line

1. Clone or download the repository
2. Build the project using `mvn clean package'
3. The service does not use an external database. It's based on files. By default the service will write texts under default `texts` directory located in the root of the project. If you want to change it then define the jvm arg: `--texts.folder=PATH_TO_YOUR_FOLDER` 
4. Start the service: `java -jar text-editor-ws/target/text-editor-ws-1.0-SNAPSHOT.jar`. Or with custom text folder: `java -jar text-editor-ws/target/text-editor-ws-1.0-SNAPSHOT.jar --texts.folder=PATH_TO_YOUR_FOLDER` 


## How to use the service

1. Open swagger-api ui: `http://localhost:8080/swagger-ui.html`
2. Here you can see all available endpoints and models and try to execute examples.

## HTTP java client

If you need to use the service from another java program, you can use client provided by the service. The usage is pretty simple. Just build and import maven module `text-editor-client`. If your service is using Spring you need to import the module in your configuration: `@Import(TextEditorClientConfig.class)` and override the service base url if necessary: `-Dtext-editor.service.base.url=YOUR_SERVICE_URL`. If you use plain java program you can create the client by your self. See a usage example in `test.maksim.editor.client.TextEditorClientUsage`. Also using the example you can try how the service works. Just open it in your IDEA and run `main` method (Note: the service should be running).

### TODO list

- Add requests validation
- Add errors handling
- Change responses. For example, `addLines` endpoint can return statuses of operations instead of `void`. Also need to handle case when text is not found.  

