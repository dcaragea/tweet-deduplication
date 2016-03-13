package com.shekhargulati.deduplication.tweet.functions;

import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import twitter4j.Status;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RemoveRetweetTextTransformationTest {

    @Test
    public void shouldRemoveRTTextFromTweet() throws Exception {
        Status status = mock(Status.class);
        when(status.getText()).thenReturn("RT @ArtofLiving: Mr. Iktomi Sha is very happy that there is so much Unity in Diversity at #WCFDay3!");
        Observable<Status> statuses = Observable.just(status);

        RemoveRetweetTextTransformation r = new RemoveRetweetTextTransformation();
        Observable<Status> obs = r.apply(statuses);
        TestSubscriber<Status> testSubscriber = new TestSubscriber<>();
        obs.subscribe(testSubscriber);
        assertThat(testSubscriber.getOnNextEvents().get(0).getText(), equalTo("Mr. Iktomi Sha is very happy that there is so much Unity in Diversity at #WCFDay3!"));
    }
}