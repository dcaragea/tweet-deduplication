tweet-deduplication [![Build Status](https://travis-ci.org/shekhargulati/tweet-deduplication.svg?branch=master)](https://travis-ci.org/shekhargulati/tweet-deduplication) [![License](https://img.shields.io/:license-mit-blue.svg)](./LICENSE.txt)
-----

`tweet-deduplication` library provides deduplication functionality over tweets. It uses `rx-tweet-stream` to get a RxJava Observable over a twitter status stream. `rx-tweet-stream` wraps Twitter4J Streaming API to provide an Observable of tweets. `tweet-deduplication` API uses JDK 8, RxJava, and Twitter4J.

The definition of duplicate depends on the application. The default implementation assumes a tweet to be a duplicate if it is talking about same URL or if it has same text. URLs are normalized using the normalizations described in [RFC 3986](https://tools.ietf.org/html/rfc3986). This allows us to determine if two syntactically different URLs are equivalent for example `https://www.github.com/?` is same as `https://github.com`. To perform URL normalization, I will use [urlcleaner](https://github.com/shekhargulati/urlcleaner) library that I have written over the weekend as well.

Getting Started
--------

To use `tweet-deduplication` in your application, you have to add `tweet-deduplication` in your classpath. tweet-deduplication is available on Maven Central so you just need to add dependency to your favorite build tool as show below.

For Apache Maven users, please add following to your pom.xml.

```xml
<dependencies>
    <dependency>
        <groupId>com.shekhargulati.reactivex</groupId>
        <artifactId>tweet-deduplication</artifactId>
        <version>0.1.0</version>
        <type>jar</type>
    </dependency>
</dependencies>
```

Gradle users can add following to their build.gradle file.

```groovy
compile(group: 'com.shekhargulati.reactivex', name: 'tweet-deduplication', version: '0.1.0', ext: 'jar')
```

## Usage

The example shown below uses the Twitter4j environment variables

```
export twitter4j.debug=true
export twitter4j.oauth.consumerKey=*********************
export twitter4j.oauth.consumerSecret=******************************************
export twitter4j.oauth.accessToken=**************************************************
export twitter4j.oauth.accessTokenSecret=******************************************
```

```java
import com.shekhargulati.reactivex.twitter.TweetStream;

TweetStream.of("java").map(Status::getText).take(10).subscribe(System.out::println);
```

If you don't want to use environment variables, then you can use the overloaded method that allows you to pass configuration object.

```java
ConfigurationBuilder cb = new ConfigurationBuilder();
cb.setDebugEnabled(true)
        .setOAuthConsumerKey("*********************")
        .setOAuthConsumerSecret("******************************************")
        .setOAuthAccessToken("**************************************************")
        .setOAuthAccessTokenSecret("******************************************");

TweetStream.of(cb.build(), "java").map(Status::getText).take(10).subscribe(System.out::println);
```

The other `of` factory methods present in `TweetStream` are:

```java
Observable<Status> of(final long... usersToFollow)
Observable<Status> of(final String[] languages, final String[] searchTerms)
Observable<Status> of(final Configuration configuration, final String[] languages, final String[] searchTerms)
Observable<Status> of(final Configuration configuration, final String... searchTerms)
Observable<Status> of(final Configuration configuration, final long... usersToFollow)
Observable<Status> of(final Configuration configuration, final String[] languages, final String[] searchTerms, final long[] usersToFollow)
```


License
-------

tweet-deduplication is licensed under the MIT License - see the `LICENSE` file for details.

Credits
---

TweetSubscriber is inspired by the TwitterObservable of [yarn starter](https://github.com/daplab/yarn-starter).
