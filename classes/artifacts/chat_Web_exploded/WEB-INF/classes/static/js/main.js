'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var exitForm = document.querySelector('#exitForm');

var stompClient = null;
var username = null;
var photo = null;
var user_Id = null;

function connect(event) {
    username = document.querySelector('#name').value.trim();
    photo = document.querySelector('#photo').value.trim();
    user_Id = document.querySelector('#user_id').value.trim();

    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
}


function onError(error) {
}


function sendMessage(event) {
    var messageContent = messageInput.value;
    if (messageContent.trim() != '') {
        if (messageContent && stompClient) {
            var chatMessage = {
                sender: username,
                content: messageInput.value,
                photo: photo,
                userId: user_Id,
                type: 'CHAT'
            };
        }
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = "";
    }
    event.preventDefault();
    return false;
}

function exit() {
    stompClient.send("/logout", {}, null);
}

function onMessageReceived(payload) {
    messageInput.value = "";
    var div = $('#chat');
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');
    var divAnswer = document.createElement('div');
    if (message.type === 'JOIN') {
        divAnswer.className = 'answer';
        divAnswer.style.textAlign = 'center';
        message.content = message.sender + ' joined!';
        divAnswer.innerHTML = '<h3 style="font-size: 15px">' + message.content + '</h3>';
    } else if (message.type === 'LEAVE') {
        divAnswer.className = 'answer';
        divAnswer.style.textAlign = 'center';
        message.content = message.sender + ' left!';
        divAnswer.innerHTML = '<h3 style="font-size: 15px">' + message.content + '</h3>';
    } else {
        var html = '<div class="avatar"><img src="' + message.photo + '"></div>' +
            '<div class="name">' + message.sender + '</div><div class="text">' + message.content + '</div>';
        if (user_Id == message.userId) {
            divAnswer.className = 'answer left';
        } else {
            divAnswer.className = 'answer right';
        }
        divAnswer.innerHTML = html;
    }
    messageElement.appendChild(divAnswer);
    messageArea.appendChild(messageElement);
    var d = div[0];
    d.scrollTop = d.scrollHeight - div.height();
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
exitForm.addEventListener('submit', exit, true);
$(document.body).keydown(function (e) {
    if (e.which == 13 && $(messageForm).focus()) {
        return sendMessage();
    }
});

