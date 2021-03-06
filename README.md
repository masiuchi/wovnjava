# WOVN.io Java Library

The WOVN.io Java library is a backend library that uses WOVN.io in order to provide translations. The WOVN.io Java library is packaged as a Servlet Filter.

This document explains the WOVN.io Java Library's install process and parameter settings.

## 1. Install Procedure

### 1.1. Create a WOVN.io account

In order to use the WOVN.io Java Library, you need a WOVN.io account.
If you do not have a WOVN.io account, first please sign up at [WOVN.io](https://wovn.io).

1.2. Adding a page to translate

Sign into [WOVN.io](https://wovn.io), and add a page you would like translated.

### 1.3. Java Application Settings

#### 1.3.1. If you're using Maven

* If you're not using Maven, please refer to https://jitpack.io/#wovnio/wovnjava.

##### 1.3.1.1. To use this library within your application, you must add the JitPack repository to your application's pom.xml file.

```XML
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
    <!-- These lines are not needed if you're using a SNAPSHOT version. -->
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
    <!-- end -->
  </repository>
  
</repositories>
```

##### 1.3.1.2. Add the WOVN.io library as a dependency to your project's pom.xml.

```XML
<dependency>
  <groupId>com.github.wovnio</groupId>
  <artifactId>wovnjava</artifactId>
  <!-- set the wovnjava version you're using here -->
  <!-- if you want to use a development version of wovnjava, set the version to "-SNAPSHOT" -->
  <version>0.1.0</version>
</dependency>
```

You can see all available versions of wovnjava [here](https://jitpack.io/#wovnio/wovnjava).

##### 1.3.1.3. Add the wovnjava library's settings to your servlet's web.xml.

```XML
<filter>
  <filter-name>wovn</filter-name>
  <filter-class>com.github.wovnio.wovnjava.WovnServletFilter</filter-class>
  <init-param>
    <param-name>userToken</param-name>
    <param-value>2Wle3</param-value><!-- set your user token -->
  </init-param>
  <init-param>
    <param-name>secretKey</param-name>
    <param-value>secret</param-value><!-- set your secret key -->
  </init-param>
</filter>

<filter-mapping>
  <filter-name>wovn</filter-name>
  <url-pattern>/*</url-pattern><!-- set the URL pattern the wovnjava library (Servlet Filter) will be applicable to -->
</filter-mapping>
```

## 2. Parameter Settings

The following parameters can be set within the WOVN.io Java Library.

Parameter Name            | Required | Default Setting
------------------------- | -------- | ------------
userToken                 | yes      | ''
secretKey                 | yes      | ''
urlPattern                | yes      | 'path'
query                     |          | ''
defaultLang               | yes      | 'en'
useProxy                  |          | 'false'
debugMode                 |          | '0'
originalUrlHeader         |          | ''
originalQueryStringHeader |          | ''
strictHtmlCheck           |          | 'false'

* A required parameter with a default setting does not need to be set within the web.xml. (Only the userToken and secretKey parameters must be set in order for the library to work)

### 2.1. userToken

Set your WOVN.io Account's user token. This parameter is required.

### 2.2. secretKey

This parameter is currently in development and is not being used. However, it is a required parameter, so make to sure to include a value in it (e.g. "secret").

### 2.3. urlPattern

Within the Java Application, the library works by adding new URL's for translation. You can set the URL type by using the Url Pattern Parameter. There are 3 URL types that can be set.

parameters  | Translated page's URL           | Notes
----------- | ------------------------------- | ------
'path'      | https://wovn.io/ja/contact      | Default Value. If no settings have been set, url_pattern defaults to this value.
'subdomain' | https://ja.wovn.io/contact      | The server's DNS settings must be configured.
'query'     | https://wovn.io/contact?wovn=ja | The least amount of changes to the application required to complete setup.

※ The previously mentioned URL's are examples of the following URL translated via the WOVN.io library. As can be seen, depending on the URL Parameter the url will change.

    https://wovn.io/contact

### 2.4. query

When WOVN.io identifies a page for translation, it ignores the query parameter. If you want to include a query parameter within the URL, you must set it using the "query" parameter. (You must set this on WOVN.io's side)

    https://wovn.io/ja/contact?os=mac&keyboard=us

If the defualt_lang is 'en', and the query is \[\] (unset), the above URL will be modified into the following URL to search for the page's translation.

    https://wovn.io/contact

If the default_lang is 'en', and the query is set to ['os'], the above URL will be modified into the following URL to search for the page's translation.

    https://wovn.io/contact?os=mac

If you want to set multiple queries, you can separate them via a comma.

### 2.5. defaultLang

This sets the Java application's default language. The default value is english ('en').

If a request is made with the default language inserted as a parameter in the URL, before the library begins translating the URL is redirected. The defaultLang parameter is used for this purpose.

If the default_lang is set to 'en', when receiving a request for the following URL,

    https://wovn.io/en/contact

The library will redirect to the following URL.

    https://wovn.io/contact

### 2.6. useProxy

When using a reverse proxy, if the WOVN.io Java Library is not given an appropriate host name, the page's translation data may not be accessible. If you set useProxy to true, during the WOVN.io Java Library's processing, it will use the HTTP Request Header's X-Forwarded-Host in order to receive the translation data.

### 2.7. debugMode

As a development feature, if debugMode is set to 1, wovnjava will output debug logs.

### 2.8. originalUrlHeader, originalQueryStringHeader

When you're using the Apache HTTP Server's mod_rewrite module, wovnjava is given the URL after rewriting. In this case, wovnjava is sometimes unable to retreive the correct translation data from the API server.

If you've configured originalUrlHeader and originalQueryStringHeader in your Application's settings file, wovnjava will use these request headers's values to retreive translation data.

Using the following Apache HTTP Server settings, if the URL (prior to rewriting) is set within the request headers,

```
SetEnvIf Request_URI "^(.*)$" REQUEST_URI=$1
RequestHeader set X-Request-Uri "%{REQUEST_URI}e"
RewriteRule .* - [E=REQUEST_QUERY_STRING:%{QUERY_STRING}]
RequestHeader set X-Query-String "%{REQUEST_QUERY_STRING}e"
```

wovnjava will use the following settings along with the correct URL (prior to rewriting) to retreive the correct translation data from the API server.

```XML
<filter>
  ...
  <init-param>
    <param-name>originalUrlHeader</param-name>
    <param-value>X-Request-Uri</param-value>
  </init-param>
  <init-param>
    <param-name>originalQueryStringHeader</param-name>
    <param-value>X-Query-String</param-value>
  </init-param>
  ...
</filter>
```
* The sample request header shown above was referenced from the following site.

https://coderwall.com/p/jhkw7w/passing-request-uri-into-request-header

### 2.9. strictHtmlCheck

(This is an experimental setting. As such, there is a possibility it will be removed in the future.)

wovnjava uses the Content-Type header to determine the data type of the response body; by default, it translates only HTML requests. If you set strictHtmlCheck to true, wovnJava determines the data type of the response body, not only by the Content-Type, but also by checking the contents of the response body. This feature is used for example when the Content-type is text/html, however the actual content is not HTML. We can then prevent unnecessary translations of the page.

wovnjava determines the response body as HTML if it starts with any of the following strings. wovnjava ignores comment tags and blanks during this process and is not case sensitive.

* <?xml
* <!DOCTYPE
* <html
