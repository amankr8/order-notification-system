package com.flykraft.service.notification;

import com.flykraft.model.notification.*;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.repository.notification.ChannelSubRepo;
import com.flykraft.repository.notification.NotifyMsgRepo;
import com.flykraft.repository.notification.NotifySubRepo;
import com.flykraft.repository.notification.StatusSubRepo;
import com.flykraft.repository.stakeholder.StakeHolderRepo;
import com.flykraft.service.stakeholder.StakeHolderService;

import java.util.*;

/**
 * Integration tests for NotificationService using ACTUAL repository implementations.
 */
public class NotificationServiceTest {

    private NotificationService notificationService;
    private NotifySubRepo notifySubRepo;
    private NotifyMsgRepo notifyMsgRepo;
    private ChannelSubRepo channelSubRepo;
    private StatusSubRepo statusSubRepo;
    private StakeHolderService stakeHolderService;
    private StakeHolderRepo stakeHolderRepo;

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        NotificationServiceTest test = new NotificationServiceTest();
        test.runAllTests();
        test.printSummary();
    }

    private void setUp() {
        // Initialize REAL repositories
        notifySubRepo = new NotifySubRepo();
        notifyMsgRepo = new NotifyMsgRepo();
        channelSubRepo = new ChannelSubRepo();
        statusSubRepo = new StatusSubRepo();
        stakeHolderRepo = new StakeHolderRepo();

        // Initialize services with real repositories
        stakeHolderService = new StakeHolderService(stakeHolderRepo);
        notificationService = new NotificationService(
                notifySubRepo,
                notifyMsgRepo,
                channelSubRepo,
                statusSubRepo,
                stakeHolderService
        );
    }

    public void runAllTests() {
        System.out.println("========== Running NotificationService Integration Tests ==========\n");

        testOptInForNotifications();
        testOptOutOfNotifications();
        testSubscribeToStatuses();
        testUnsubscribeFromStatuses();
        testSubscribeToChannels();
        testUnsubscribeFromChannels();
        testUpdateStatusMessageForCategory();
        testSubscribeToOrder();
        testUnsubscribeFromOrder();
        testGetMessageByCategoryAndStatusId();
        testValidateStatusSubscriptionByStakeHolder();
        testGetChannelSubscriptionsByStakeHolderId();

        System.out.println("\n========== Test Summary ==========");
    }

    // Test 1: Opt-in for notifications
    private void testOptInForNotifications() {
        setUp();
        System.out.println("Test 1: testOptInForNotifications");

        try {
            StakeHolder stakeHolder = new StakeHolder("John Doe", 1);
            StakeHolder savedStakeHolder = stakeHolderService.createStakeHolder(stakeHolder);
            stakeHolderService.updateStakeHolder(savedStakeHolder);
            savedStakeHolder.setOptedInForNotifications(false);
            stakeHolderService.updateStakeHolder(savedStakeHolder);

            notificationService.optInForNotifications(savedStakeHolder.getStakeHolderId());

            StakeHolder updatedStakeHolder = stakeHolderService.getStakeHolderById(savedStakeHolder.getStakeHolderId());
            assertTrue(updatedStakeHolder.hasOptedInForNotifications(), "StakeHolder should be opted in");
            System.out.println("✓ PASSED: StakeHolder successfully opted in for notifications\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 2: Opt-out of notifications
    private void testOptOutOfNotifications() {
        setUp();
        System.out.println("Test 2: testOptOutOfNotifications");

        try {
            StakeHolder stakeHolder = new StakeHolder("Jane Doe", 2);
            StakeHolder savedStakeHolder = stakeHolderService.createStakeHolder(stakeHolder);

            notificationService.optOutOfNotifications(savedStakeHolder.getStakeHolderId());

            StakeHolder updatedStakeHolder = stakeHolderService.getStakeHolderById(savedStakeHolder.getStakeHolderId());
            assertFalse(updatedStakeHolder.hasOptedInForNotifications(), "StakeHolder should be opted out");
            System.out.println("✓ PASSED: StakeHolder successfully opted out of notifications\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: Subscribe to statuses
    private void testSubscribeToStatuses() {
        setUp();
        System.out.println("Test 3: testSubscribeToStatuses");

        try {
            Integer stakeHolderId = 100;
            Set<Integer> statusIds = new HashSet<>(Arrays.asList(1, 2, 3));

            notificationService.subscribeToStatuses(stakeHolderId, statusIds);

            List<StatusSub> subs = statusSubRepo.findByStakeHolderId(stakeHolderId);
            assertEquals(3, subs.size(), "Should have 3 status subscriptions");
            System.out.println("✓ PASSED: StakeHolder successfully subscribed to 3 statuses\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: Unsubscribe from statuses
    private void testUnsubscribeFromStatuses() {
        setUp();
        System.out.println("Test 4: testUnsubscribeFromStatuses");

        try {
            Integer stakeHolderId = 101;
            Set<Integer> initialStatusIds = new HashSet<>(Arrays.asList(1, 2, 3));
            notificationService.subscribeToStatuses(stakeHolderId, initialStatusIds);

            Set<Integer> unsubscribeIds = new HashSet<>(Arrays.asList(1, 2));
            notificationService.unsubscribeFromStatuses(stakeHolderId, unsubscribeIds);

            List<StatusSub> subs = statusSubRepo.findByStakeHolderId(stakeHolderId);
            assertEquals(1, subs.size(), "Should have 1 status subscription remaining");
            System.out.println("✓ PASSED: StakeHolder successfully unsubscribed from 2 statuses\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Subscribe to channels
    private void testSubscribeToChannels() {
        setUp();
        System.out.println("Test 5: testSubscribeToChannels");

        try {
            Integer stakeHolderId = 102;
            Set<Integer> channelIds = new HashSet<>(Arrays.asList(1, 2));

            notificationService.subscribeToChannels(stakeHolderId, channelIds);

            List<ChannelSub> subs = channelSubRepo.findByStakeHolderId(stakeHolderId);
            assertEquals(2, subs.size(), "Should have 2 channel subscriptions");
            System.out.println("✓ PASSED: StakeHolder successfully subscribed to 2 channels\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 6: Unsubscribe from channels
    private void testUnsubscribeFromChannels() {
        setUp();
        System.out.println("Test 6: testUnsubscribeFromChannels");

        try {
            Integer stakeHolderId = 103;
            Set<Integer> initialChannelIds = new HashSet<>(Arrays.asList(1, 2));
            notificationService.subscribeToChannels(stakeHolderId, initialChannelIds);

            Set<Integer> unsubscribeIds = new HashSet<>(Collections.singletonList(1));
            notificationService.unsubscribeFromChannels(stakeHolderId, unsubscribeIds);

            List<ChannelSub> subs = channelSubRepo.findByStakeHolderId(stakeHolderId);
            assertEquals(1, subs.size(), "Should have 1 channel subscription remaining");
            System.out.println("✓ PASSED: StakeHolder successfully unsubscribed from 1 channel\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 7: Update status message for category
    private void testUpdateStatusMessageForCategory() {
        setUp();
        System.out.println("Test 7: testUpdateStatusMessageForCategory");

        try {
            Integer categoryId = 50;
            Integer statusId = 50;
            String newMessage = "Order Successfully Placed";

            NotifyMsg notifyMsg = new NotifyMsg(categoryId, statusId, "Order Placed");
            notifyMsgRepo.save(notifyMsg);

            notificationService.updateStatusMessageForCategory(categoryId, statusId, newMessage);

            List<NotifyMsg> msgs = notifyMsgRepo.findByCategoryId(categoryId);
            boolean found = msgs.stream()
                    .anyMatch(msg -> msg.getOrderStatusId().equals(statusId) && msg.getMessage().equals(newMessage));
            assertTrue(found, "Message should be updated");
            System.out.println("✓ PASSED: Status message successfully updated\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 8: Subscribe to order
    private void testSubscribeToOrder() {
        setUp();
        System.out.println("Test 8: testSubscribeToOrder");

        try {
            Integer stakeHolderId = 104;
            Integer orderId = 500;

            notificationService.subscribeToOrder(stakeHolderId, orderId);

            List<NotifySub> subs = notifySubRepo.findByOrderId(orderId);
            assertEquals(1, subs.size(), "Should have 1 order subscription");
            assertEquals(stakeHolderId, subs.getFirst().getStakeHolderId(), "StakeHolder ID should match");
            System.out.println("✓ PASSED: StakeHolder successfully subscribed to order\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 9: Unsubscribe from order
    private void testUnsubscribeFromOrder() {
        setUp();
        System.out.println("Test 9: testUnsubscribeFromOrder");

        try {
            Integer stakeHolderId1 = 105;
            Integer stakeHolderId2 = 106;
            Integer orderId = 501;

            notificationService.subscribeToOrder(stakeHolderId1, orderId);
            notificationService.subscribeToOrder(stakeHolderId2, orderId);

            notificationService.unsubscribeFromOrder(stakeHolderId1, orderId);

            List<NotifySub> subs = notifySubRepo.findByOrderId(orderId);
            assertEquals(1, subs.size(), "Should have 1 order subscription remaining");
            assertEquals(stakeHolderId2, subs.getFirst().getStakeHolderId(), "Remaining subscriber should be stakeHolderId2");
            System.out.println("✓ PASSED: StakeHolder successfully unsubscribed from order\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 10: Get message by category and status ID
    private void testGetMessageByCategoryAndStatusId() {
        setUp();
        System.out.println("Test 10: testGetMessageByCategoryAndStatusId");

        try {
            Integer categoryId = 51;
            Integer statusId = 51;
            String expectedMessage = "Your order is being prepared";

            NotifyMsg notifyMsg = new NotifyMsg(categoryId, statusId, expectedMessage);
            notifyMsgRepo.save(notifyMsg);

            String message = notificationService.getMessageByCategoryAndStatusId(categoryId, statusId);
            assertEquals(expectedMessage, message, "Message should match");
            System.out.println("✓ PASSED: Retrieved correct message for category and status\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 11: Validate status subscription by stakeholder
    private void testValidateStatusSubscriptionByStakeHolder() {
        setUp();
        System.out.println("Test 11: testValidateStatusSubscriptionByStakeHolder");

        try {
            Integer statusId = 4;
            StakeHolder stakeHolder = new StakeHolder("Test User", 1);
            StakeHolder savedStakeHolder = stakeHolderService.createStakeHolder(stakeHolder);

            notificationService.subscribeToStatuses(savedStakeHolder.getStakeHolderId(), new HashSet<>(Collections.singletonList(statusId)));

            boolean isSubscribed = notificationService.validateStatusSubscriptionByStakeHolder(savedStakeHolder, statusId);
            assertTrue(isSubscribed, "StakeHolder should be subscribed to the status");
            System.out.println("✓ PASSED: Status subscription validation works correctly\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 12: Get channel subscriptions by stakeholder ID
    private void testGetChannelSubscriptionsByStakeHolderId() {
        setUp();
        System.out.println("Test 12: testGetChannelSubscriptionsByStakeHolderId");

        try {
            Integer stakeHolderId = 107;
            Set<Integer> channelIds = new HashSet<>(Arrays.asList(1, 2));

            notificationService.subscribeToChannels(stakeHolderId, channelIds);

            List<Channel> channels = notificationService.getChannelSubscriptionsByStakeHolderId(stakeHolderId);
            assertEquals(2, channels.size(), "Should have 2 channel subscriptions");
            System.out.println("✓ PASSED: Retrieved channel subscriptions correctly\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Assertion methods
    private void assertTrue(boolean condition, String message) throws AssertionError {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private void assertFalse(boolean condition, String message) throws AssertionError {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    private void assertEquals(Object expected, Object actual, String message) throws AssertionError {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(message + " | Expected: " + expected + ", Actual: " + actual);
        }
    }

    private void printSummary() {
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));
        System.out.println("==================================\n");

        if (testsFailed == 0) {
            System.out.println("✓ All tests passed successfully!");
        } else {
            System.out.println("✗ Some tests failed. Please review the output above.");
        }
    }
}

