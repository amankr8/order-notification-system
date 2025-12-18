package com.flykraft.service.notification;

import com.flykraft.model.notification.*;
import com.flykraft.model.stakeholder.StakeHolder;
import com.flykraft.repository.notification.ChannelSubRepo;
import com.flykraft.repository.notification.NotifyMsgRepo;
import com.flykraft.repository.notification.NotifySubRepo;
import com.flykraft.repository.notification.StatusSubRepo;
import com.flykraft.service.stakeholder.StakeHolderService;

import java.util.*;

/**
 * Simple test cases for NotificationService without any testing frameworks.
 * Uses simplified mock implementations of repositories and services.
 */
public class NotificationServiceTest {

    private NotificationService notificationService;
    private InMemoryNotifySubRepo mockNotifySubRepo;
    private InMemoryNotifyMsgRepo mockNotifyMsgRepo;
    private InMemoryChannelSubRepo mockChannelSubRepo;
    private InMemoryStatusSubRepo mockStatusSubRepo;
    private MockStakeHolderService mockStakeHolderService;

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        NotificationServiceTest test = new NotificationServiceTest();
        test.runAllTests();
        test.printSummary();
    }

    private void setUp() {
        mockNotifySubRepo = new InMemoryNotifySubRepo();
        mockNotifyMsgRepo = new InMemoryNotifyMsgRepo();
        mockChannelSubRepo = new InMemoryChannelSubRepo();
        mockStatusSubRepo = new InMemoryStatusSubRepo();
        mockStakeHolderService = new MockStakeHolderService();

        notificationService = new NotificationService(
                mockNotifySubRepo,
                mockNotifyMsgRepo,
                mockChannelSubRepo,
                mockStatusSubRepo,
                mockStakeHolderService
        );
    }

    public void runAllTests() {
        System.out.println("========== Running NotificationService Tests ==========\n");

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
            stakeHolder.setStakeHolderId(1);
            stakeHolder.setOptedInForNotifications(false);
            mockStakeHolderService.addStakeHolder(stakeHolder);

            notificationService.optInForNotifications(1);

            StakeHolder updatedStakeHolder = mockStakeHolderService.getStakeHolderById(1);
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
            stakeHolder.setStakeHolderId(2);
            stakeHolder.setOptedInForNotifications(true);
            mockStakeHolderService.addStakeHolder(stakeHolder);

            notificationService.optOutOfNotifications(2);

            StakeHolder updatedStakeHolder = mockStakeHolderService.getStakeHolderById(2);
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
            Integer stakeHolderId = 3;
            Set<Integer> statusIds = new HashSet<>(Arrays.asList(1, 2, 3));

            notificationService.subscribeToStatuses(stakeHolderId, statusIds);

            List<StatusSub> subs = mockStatusSubRepo.findByStakeHolderId(stakeHolderId);
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
            Integer stakeHolderId = 4;
            Set<Integer> initialStatusIds = new HashSet<>(Arrays.asList(1, 2, 3));
            notificationService.subscribeToStatuses(stakeHolderId, initialStatusIds);

            Set<Integer> unsubscribeIds = new HashSet<>(Arrays.asList(1, 2));
            notificationService.unsubscribeFromStatuses(stakeHolderId, unsubscribeIds);

            List<StatusSub> subs = mockStatusSubRepo.findByStakeHolderId(stakeHolderId);
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
            Integer stakeHolderId = 5;
            Set<Integer> channelIds = new HashSet<>(Arrays.asList(1, 2));

            notificationService.subscribeToChannels(stakeHolderId, channelIds);

            List<ChannelSub> subs = mockChannelSubRepo.findByStakeHolderId(stakeHolderId);
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
            Integer stakeHolderId = 6;
            Set<Integer> initialChannelIds = new HashSet<>(Arrays.asList(1, 2));
            notificationService.subscribeToChannels(stakeHolderId, initialChannelIds);

            Set<Integer> unsubscribeIds = new HashSet<>(Collections.singletonList(1));
            notificationService.unsubscribeFromChannels(stakeHolderId, unsubscribeIds);

            List<ChannelSub> subs = mockChannelSubRepo.findByStakeHolderId(stakeHolderId);
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
            Integer categoryId = 10; // Use unique category ID to avoid default messages
            Integer statusId = 10;
            String originalMessage = "Order Placed";
            String newMessage = "Order Successfully Placed";

            NotifyMsg notifyMsg = new NotifyMsg(categoryId, statusId, originalMessage);
            notifyMsg.setNotifyMsgId(100);
            mockNotifyMsgRepo.save(notifyMsg);

            notificationService.updateStatusMessageForCategory(categoryId, statusId, newMessage);

            List<NotifyMsg> msgs = mockNotifyMsgRepo.findByCategoryId(categoryId);
            assertEquals(1, msgs.size(), "Should have 1 message for this category");
            assertEquals(newMessage, msgs.getFirst().getMessage(), "Message should be updated");
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
            Integer stakeHolderId = 7;
            Integer orderId = 101;

            notificationService.subscribeToOrder(stakeHolderId, orderId);

            List<NotifySub> subs = mockNotifySubRepo.findByOrderId(orderId);
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
            Integer stakeHolderId1 = 8;
            Integer stakeHolderId2 = 9;
            Integer orderId = 102;

            notificationService.subscribeToOrder(stakeHolderId1, orderId);
            notificationService.subscribeToOrder(stakeHolderId2, orderId);

            notificationService.unsubscribeFromOrder(stakeHolderId1, orderId);

            List<NotifySub> subs = mockNotifySubRepo.findByOrderId(orderId);
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
            Integer categoryId = 20; // Use unique category ID to avoid default messages
            Integer statusId = 20;
            String expectedMessage = "Your order is being prepared";

            NotifyMsg notifyMsg = new NotifyMsg(categoryId, statusId, expectedMessage);
            notifyMsg.setNotifyMsgId(200);
            mockNotifyMsgRepo.save(notifyMsg);

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
            Integer stakeHolderId = 10;
            Integer statusId = 3;

            notificationService.subscribeToStatuses(stakeHolderId, new HashSet<>(Collections.singletonList(statusId)));

            StakeHolder stakeHolder = new StakeHolder("Test User", 1);
            stakeHolder.setStakeHolderId(stakeHolderId);
            mockStakeHolderService.addStakeHolder(stakeHolder);

            boolean isSubscribed = notificationService.validateStatusSubscriptionByStakeHolder(stakeHolder, statusId);
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
            Integer stakeHolderId = 11;
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

    // ==================== In-Memory Mock Implementations ====================

    /**
     * Simple in-memory implementation of NotifySubRepo for testing
     */
    private static class InMemoryNotifySubRepo extends NotifySubRepo {
        private final Map<Integer, NotifySub> data = new HashMap<>();
        private int idCounter = 1;

        @Override
        public NotifySub save(NotifySub notifySub) {
            if (notifySub.getNotifySubId() == null) {
                notifySub.setNotifySubId(idCounter++);
            }
            data.put(notifySub.getNotifySubId(), notifySub);
            return notifySub;
        }

        @Override
        public List<NotifySub> findByOrderId(Integer orderId) {
            List<NotifySub> result = new ArrayList<>();
            for (NotifySub sub : data.values()) {
                if (sub.getOrderId().equals(orderId)) {
                    result.add(sub);
                }
            }
            return result;
        }

        @Override
        public void deleteById(Integer id) {
            data.remove(id);
        }

        @Override
        public Optional<NotifySub> findById(Integer id) {
            return Optional.ofNullable(data.get(id));
        }
    }

    /**
     * Simple in-memory implementation of NotifyMsgRepo for testing
     */
    private static class InMemoryNotifyMsgRepo extends NotifyMsgRepo {
        private final Map<Integer, NotifyMsg> data = new HashMap<>();
        private int idCounter = 1;

        @Override
        public NotifyMsg save(NotifyMsg notifyMsg) {
            if (notifyMsg.getNotifyMsgId() == null) {
                notifyMsg.setNotifyMsgId(idCounter++);
            }
            data.put(notifyMsg.getNotifyMsgId(), notifyMsg);
            return notifyMsg;
        }

        @Override
        public List<NotifyMsg> findByCategoryId(Integer categoryId) {
            List<NotifyMsg> result = new ArrayList<>();
            for (NotifyMsg msg : data.values()) {
                if (msg.getStakeHolderCategoryId().equals(categoryId)) {
                    result.add(msg);
                }
            }
            return result;
        }

        @Override
        public void deleteById(Integer id) {
            data.remove(id);
        }

        @Override
        public Optional<NotifyMsg> findById(Integer id) {
            return Optional.ofNullable(data.get(id));
        }
    }

    /**
     * Simple in-memory implementation of ChannelSubRepo for testing
     */
    private static class InMemoryChannelSubRepo extends ChannelSubRepo {
        private final Map<Integer, ChannelSub> data = new HashMap<>();
        private int idCounter = 1;

        @Override
        public ChannelSub save(ChannelSub channelSub) {
            if (channelSub.getChannelSubId() == null) {
                channelSub.setChannelSubId(idCounter++);
            }
            data.put(channelSub.getChannelSubId(), channelSub);
            return channelSub;
        }

        @Override
        public List<ChannelSub> findByStakeHolderId(Integer stakeHolderId) {
            List<ChannelSub> result = new ArrayList<>();
            for (ChannelSub sub : data.values()) {
                if (sub.getStakeHolderId().equals(stakeHolderId)) {
                    result.add(sub);
                }
            }
            return result;
        }

        @Override
        public void deleteById(Integer id) {
            data.remove(id);
        }

        @Override
        public Optional<ChannelSub> findById(Integer id) {
            return Optional.ofNullable(data.get(id));
        }
    }

    /**
     * Simple in-memory implementation of StatusSubRepo for testing
     */
    private static class InMemoryStatusSubRepo extends StatusSubRepo {
        private final Map<Integer, StatusSub> data = new HashMap<>();
        private int idCounter = 1;

        @Override
        public StatusSub save(StatusSub statusSub) {
            if (statusSub.getStatusSubId() == null) {
                statusSub.setStatusSubId(idCounter++);
            }
            data.put(statusSub.getStatusSubId(), statusSub);
            return statusSub;
        }

        @Override
        public List<StatusSub> findByStakeHolderId(Integer stakeHolderId) {
            List<StatusSub> result = new ArrayList<>();
            for (StatusSub sub : data.values()) {
                if (sub.getStakeHolderId().equals(stakeHolderId)) {
                    result.add(sub);
                }
            }
            return result;
        }

        @Override
        public void deleteById(Integer id) {
            data.remove(id);
        }

        @Override
        public Optional<StatusSub> findById(Integer id) {
            return Optional.ofNullable(data.get(id));
        }
    }

    /**
     * Simple mock implementation of StakeHolderService for testing
     */
    private static class MockStakeHolderService extends StakeHolderService {
        private final Map<Integer, StakeHolder> data = new HashMap<>();

        public MockStakeHolderService() {
            super(null);
        }

        public void addStakeHolder(StakeHolder stakeHolder) {
            data.put(stakeHolder.getStakeHolderId(), stakeHolder);
        }

        @Override
        public StakeHolder getStakeHolderById(Integer id) {
            StakeHolder stakeHolder = data.get(id);
            if (stakeHolder == null) {
                throw new RuntimeException("Stakeholder does not exist");
            }
            return stakeHolder;
        }

        @Override
        public StakeHolder updateStakeHolder(StakeHolder stakeHolder) {
            data.put(stakeHolder.getStakeHolderId(), stakeHolder);
            return stakeHolder;
        }

        @Override
        public StakeHolder createStakeHolder(StakeHolder stakeHolder) {
            if (stakeHolder.getStakeHolderId() == null) {
                stakeHolder.setStakeHolderId(data.size() + 1);
            }
            data.put(stakeHolder.getStakeHolderId(), stakeHolder);
            return stakeHolder;
        }
    }
}

