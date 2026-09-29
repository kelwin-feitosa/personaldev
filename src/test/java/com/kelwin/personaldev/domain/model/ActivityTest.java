package com.kelwin.personaldev.domain.model;

import com.kelwin.personaldev.domain.model.enums.ActivityStatus;
import com.kelwin.personaldev.presentation.exception.DomainRuleException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActivityTest {

    private Activity createActivity(ActivityStatus status) {
        return Activity.builder()
                .status(status)
                .build();
    }

    @Test
    void shouldStartPendingActivity() {
        Activity activity = createActivity(ActivityStatus.PENDING);

        activity.start();

        assertEquals(ActivityStatus.IN_PROGRESS, activity.getStatus());
    }

    @Test
    void shouldNotStartInProgressActivity() {
        Activity activity = createActivity(ActivityStatus.IN_PROGRESS);

        assertThrows(
                DomainRuleException.class,
                activity::start
        );
    }

    @Test
    void shouldNotStartCompletedActivity() {
        Activity activity = createActivity(ActivityStatus.COMPLETED);

        assertThrows(
                DomainRuleException.class,
                activity::start
        );
    }

    @Test
    void shouldNotStartCancelledActivity() {
        Activity activity = createActivity(ActivityStatus.CANCELLED);

        assertThrows(
                DomainRuleException.class,
                activity::start
        );
    }

    @Test
    void shouldCompleteInProgressActivity() {
        Activity activity = createActivity(ActivityStatus.IN_PROGRESS);

        activity.complete();

        assertEquals(ActivityStatus.COMPLETED, activity.getStatus());
    }

    @Test
    void shouldNotCompletePendingActivity() {
        Activity activity = createActivity(ActivityStatus.PENDING);

        assertThrows(
                DomainRuleException.class,
                activity::complete
        );
    }

    @Test
    void shouldNotCompleteCompletedActivity() {
        Activity activity = createActivity(ActivityStatus.COMPLETED);

        assertThrows(
                DomainRuleException.class,
                activity::complete
        );
    }

    @Test
    void shouldNotCompleteCancelledActivity() {
        Activity activity = createActivity(ActivityStatus.CANCELLED);

        assertThrows(
                DomainRuleException.class,
                activity::complete
        );
    }

    @Test
    void shouldCancelPendingActivity() {
        Activity activity = createActivity(ActivityStatus.PENDING);

        activity.cancel();

        assertEquals(ActivityStatus.CANCELLED, activity.getStatus());
    }

    @Test
    void shouldCancelInProgressActivity() {
        Activity activity = createActivity(ActivityStatus.IN_PROGRESS);

        activity.cancel();

        assertEquals(ActivityStatus.CANCELLED, activity.getStatus());
    }

    @Test
    void shouldNotCancelCompletedActivity() {
        Activity activity = createActivity(ActivityStatus.COMPLETED);

        assertThrows(
                DomainRuleException.class,
                activity::cancel
        );
    }

    @Test
    void shouldNotCancelCancelledActivity() {
        Activity activity = createActivity(ActivityStatus.CANCELLED);

        assertThrows(
                DomainRuleException.class,
                activity::cancel
        );
    }
}