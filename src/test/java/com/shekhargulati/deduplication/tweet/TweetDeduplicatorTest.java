package com.shekhargulati.deduplication.tweet;

import com.shekhargulati.deduplication.tweet.repository.TextHashRepository;
import com.shekhargulati.deduplication.tweet.repository.UrlRepository;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import twitter4j.Status;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TweetDeduplicatorTest {

    private TweetDeDuplicator deDuplicator;
    private Status mockStatus1;
    private Status mockStatus2;

    @Before
    public void setUp() throws Exception {
        UrlRepository urlRepository = UrlRepository.getDefaultRepository();
        TextHashRepository textHashRepository = TextHashRepository.getDefaultRepository();
        deDuplicator = new TweetDeDuplicator(urlRepository, textHashRepository);
        mockStatus1 = mock(Status.class);
        mockStatus2 = mock(Status.class);
    }

    @Test
    public void shouldDeduplicateTweetsWithSameUrlAndText() throws Exception {
        when(mockStatus1.getText()).thenReturn("Reading about https://t.co/d4Y4zuKS83");
        when(mockStatus2.getText()).thenReturn("Reading about https://t.co/d4Y4zuKS83");
        Observable<Status> statuses = Observable.just(mockStatus1, mockStatus2);

        Observable<Status> deduplicatedObs = deDuplicator.deduplicate(statuses);
        makeAssertionsUsingTestSubscriber(deduplicatedObs);
    }

    @Test
    public void shouldDeduplicateTweetsWithSameText() throws Exception {
        when(mockStatus1.getText()).thenReturn("Mr. Iktomi Sha is very happy that there is so much Unity in Diversity at #WCFDay3!");
        when(mockStatus2.getText()).thenReturn("Mr. Iktomi Sha is very happy that there is so much Unity in Diversity at #WCFDay3!");
        Observable<Status> statuses = Observable.just(mockStatus1, mockStatus2);

        Observable<Status> deduplicatedObs = deDuplicator.deduplicate(statuses);

        makeAssertionsUsingTestSubscriber(deduplicatedObs);
    }

    @Test
    public void shouldDeduplicateTweetsWithSameUrlAndDifferentText() throws Exception {
        when(mockStatus1.getText()).thenReturn("Reading about https://t.co/d4Y4zuKS83");
        when(mockStatus2.getText()).thenReturn("\"Reading from https://t.co/d4Y4zuKS83");
        Observable<Status> statuses = Observable.just(mockStatus1, mockStatus2);

        Observable<Status> deduplicatedObs = deDuplicator.deduplicate(statuses);
        makeAssertionsUsingTestSubscriber(deduplicatedObs);
    }


    @Test
    public void shouldDeduplicateTweetsWithRTTextInTheBeginning() throws Exception {
        when(mockStatus1.getText()).thenReturn("RT @ArtofLiving: Mr. Iktomi Sha is very happy that there is so much Unity in Diversity at #WCFDay3!");
        when(mockStatus2.getText()).thenReturn("Mr. Iktomi Sha is very happy that there is so much Unity in Diversity at #WCFDay3!");
        Observable<Status> statuses = Observable.just(mockStatus1, mockStatus2);
        Observable<Status> deduplicatedObs = deDuplicator.deduplicate(statuses);
        makeAssertionsUsingTestSubscriber(deduplicatedObs);
    }

    @Test
    public void shouldDeduplicateTweetsWithCaseDifference() throws Exception {
        when(mockStatus1.getText()).thenReturn("RT @ArtofLiving: Mr. Iktomi Sha is very Happy that there is so much unity in DIVERSITY at #WCFDay3!");
        when(mockStatus2.getText()).thenReturn("Mr. Iktomi Sha is very happy that there is so much Unity in Diversity at #WCFDay3!");
        Observable<Status> statuses = Observable.just(mockStatus1, mockStatus2);
        Observable<Status> deduplicatedObs = deDuplicator.deduplicate(statuses);
        makeAssertionsUsingTestSubscriber(deduplicatedObs);
    }

    @Test
    public void shouldDeduplicateNormalizedUrls() throws Exception {
        when(mockStatus1.getText()).thenReturn("Learning 52 technologies in 2016 https://github.com/shekhargulati/52-technologies-in-2016");
        when(mockStatus2.getText()).thenReturn("Learning 52 technologies in 2016 https://www.github.com/shekhargulati/52-technologies-in-2016");
        Observable<Status> statuses = Observable.just(mockStatus1, mockStatus2);
        Observable<Status> deduplicatedObs = deDuplicator.deduplicate(statuses);
        makeAssertionsUsingTestSubscriber(deduplicatedObs);
    }

    private void makeAssertionsUsingTestSubscriber(Observable<Status> deduplicatedObs) {
        TestSubscriber<Status> testSubscriber = new TestSubscriber<>();
        deduplicatedObs.subscribe(testSubscriber);
        assertThat(testSubscriber.getOnNextEvents(), hasSize(1));
        assertThat(testSubscriber.getOnCompletedEvents(), hasSize(1));
        assertThat(testSubscriber.getOnErrorEvents(), hasSize(0));
    }
}
