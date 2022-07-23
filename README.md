# Custom-MIME-types Example Using Custom JaxRS MessageBodyWriter
Demo project to handle REST requests with Multiple MIME Types like json &amp; protobuf in the same API's.

# Sample REST Resource:
    @GET
    @Path("/proto")
    @Produces({MediaType.APPLICATION_JSON, MediaTypeExt.APPLICATION_X_PROTOBUF})
    public Response testProto()
    
    @POST
    @Path("/proto")
    @Consumes(MediaTypeExt.APPLICATION_X_PROTOBUF)
    @Produces(MediaTypeExt.APPLICATION_X_PROTOBUF)
    public Response createProto(PersonBinding.Person john)
    
# Sample Protobuf MessageBodyWriter

    @Provider
    @Produces(MediaTypeExt.APPLICATION_X_PROTOBUF)
    public class ProtoMessageWriter implements MessageBodyWriter<PersonBinding.Person> {
      private final static Logger logger = LoggerFactory.getLogger(ProtoMessageReader.class);

      @Override
      public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
          return aClass == PersonBinding.Person.class;
      }

    @Override
      public void writeTo(PersonBinding.Person person, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object>      multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
          multivaluedMap.add(HttpHeaders.CONTENT_LENGTH,person.getSerializedSize());
          logger.info("Proto Response Size: {}",person.getSerializedSize());
          person.writeTo(outputStream);
      }
    }
