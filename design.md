`Text-editor-service` is a web service for managing edit and retrieve operation on texts. The service is written in Java and based on spring-boot framework. Maven is using to building the project. The service contains 3 modules:
- `text-editor-client`: this is a java client which can be imported and used in another java application for simple communication with the service 
- `text-editor-common`: common module which contains DTOs and constants needed for `client` ans `ws` modules 
- `text-editor-ws`: REST web service

## text-editor-ws

The service allows to execute following operations on texts:

- Create lines of text
- Modify lines
- Add lines
- Delete lines
- Fetch lines 
- Concatenate lines into a text
- Search lines

The service also provides a swagger API which is available by address: SERVICE_URL/swagger-ui.html (e.q. http://localhost:8080/swagger-ui.html).

The entry point of the service is located in `test.maksim.editor.ws.rest.EditorController`. The main purpose of the controller is to receive HTTP requests and then pass them to the services:
- `test.maksim.editor.ws.service.EditorService`: responsible for edit/fetch operations.
- `test.maksim.editor.ws.service.LinesSearchService` with simple implementation `FileLinesSearchService`: responsible for search. This is a very primitive and not efficient search service. In real life, texts should be stored in a search engine like Solr, Elasticsearch in order to provide more efficient search with wide functionality.

For manipulation with storage there is `test.maksim.editor.ws.repository.TextRepository`. Currently is has single implementation `FileTextRepository` based on files. The idea is each text has its own directory created with random UUID. And each line stored in a single file. A filename is a line which saves the order. Storing texts in this way allow us to efficiently manipulate with text. We can easily get a line by a line number(s), modify line by line number. And we can maintain very large lines and texts. Here we can use local file storage or put files in a cloud (e.q. Amason S3). An alternative implementation is to store texts in a database. 
 
## text-editor-client

If the service is using from another java program, there is the java client provided by the service. If a service is based on Spring the only thing what need to do is to import the module in a configuration (e.q. `Application` class): `@Import(TextEditorClientConfig.class)` and override the service base url if necessary: `-Dtext-editor.service.base.url=YOUR_SERVICE_URL`. In case if another component is a plain java program it's possible to create the client by your self. See a usage example in `test.maksim.editor.client.TextEditorClientUsage`.