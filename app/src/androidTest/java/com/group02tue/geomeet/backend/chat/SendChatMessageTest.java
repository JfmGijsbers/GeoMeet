package com.group02tue.geomeet.backend.chat;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group02tue.geomeet.backend.authentication.AbstractAuthenticatedTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class SendChatMessageTest extends AbstractAuthenticatedTestCase {

    private ChatManager chatManager;

    @Before
    public void initializeChat() {
        chatManager = new ChatManager(applicationContext);

    }

    @Test
    public void testSend() {
        ChatEventListener listener = new ChatEventListener() {
            @Override
            public void onNewMessageReceived(ChatMessage message) {
            }

            @Override
            public void onMessageSent(ChatMessage message) {
            }

            @Override
            public void onFailedToSendMessage(ChatMessage message, String reason) {
                fail("Failed to send message");
            }
        };

        chatManager.addListener(listener);
        chatManager.sendMessage(authenticationManager, "you", "test");
        chatManager.removeListener(listener);
    }
}
