package com.example.demo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add ProtobufHttpMessageConverter.
        // Spring Boot typically auto-configures this if protobuf-java is present.
        // However, explicitly adding it can help ensure it's prioritized or correctly configured,
        // especially if there were conflicts or custom media type needs.
        // The default ProtobufHttpMessageConverter should handle "application/x-protobuf"
        // and "application/protobuf".
        converters.add(new ProtobufHttpMessageConverter());
    }

    // If further customization of ProtobufHttpMessageConverter is needed,
    // for example, to use a specific ProtobufJsonFormat parser/printer or to add more media types:
    //
    // @Bean
    // public ProtobufHttpMessageConverter protobufHttpMessageConverter() {
    //     ProtobufHttpMessageConverter converter = new ProtobufHttpMessageConverter();
    //     // Example: Customize supported media types if needed
    //     // converter.setSupportedMediaTypes(List.of(MediaType.valueOf("application/x-protobuf"), MediaType.APPLICATION_JSON));
    //     // Example: If using protobuf-java-util for JSON format
    //     // com.google.protobuf.util.JsonFormat.Parser parser = com.google.protobuf.util.JsonFormat.parser().ignoringUnknownFields();
    //     // com.google.protobuf.util.JsonFormat.Printer printer = com.google.protobuf.util.JsonFormat.printer().preservingProtoFieldNames();
    //     // return new ProtobufHttpMessageConverter(parser, printer);
    //     return converter;
    // }
    //
    // And then in configureMessageConverters:
    // @Autowired
    // private ProtobufHttpMessageConverter protobufHttpMessageConverter;
    //
    // @Override
    // public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    //     converters.add(protobufHttpMessageConverter);
    // }
    // For now, the simple addition of a new instance is often enough to ensure it's registered.
}
