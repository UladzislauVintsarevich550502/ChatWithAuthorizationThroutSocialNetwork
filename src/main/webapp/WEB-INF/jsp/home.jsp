<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<jsp:useBean class="bsuir.vintsarevich.task.entity.Message" scope="session" id="message"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
    <title>My Chat</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.nicescroll/3.6.8-fix/jquery.nicescroll.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
</head>
<body>
<noscript>
    <h2>Sorry! Your browser doesn't support Javascript</h2>
</noscript>

<div class="container" id="username-page">
    <form id="usernameForm" name="usernameForm">
        <img src="${photo}" id="userPhoto" class="rounded-circle"
             alt="Cinque Terre"
             width="300" height="300">
        <input type="text" id="name" value="${nickname}" autocomplete="off" class="form-control" disabled/>
        <input type="text" style="visibility: hidden; display: none;" id="photo" value="${photo}">
        <input type="text" style="visibility: hidden; display: none;" id="user_id" value="${userId}">
        <button type="submit" class="btn btn-primary" style="font-size: 15px">Start Chatting</button>
    </form>
</div>

<div id="chat-page" class="hidden" class="content container-fluid bootstrap snippets">
    <div class="row row-broken">
        <div id="header">
            <h2 id="a">Mini Chat</h2>
            <form id="exitForm">
                <input type="submit" value="Exit" class="button">
            </form>
        </div>
        <div class="col-sm-9 col-xs-12 chat" id="chat" style="overflow: hidden; outline: none;" tabindex="5001">
            <div class="col-inside-lg decor-default">
                <div class="chat-body">
                    <ul id="messageArea">
                        <c:choose>
                            <c:when test="${messageList!=null}">
                                <c:forEach var="message" items="${messageList}">
                                    <c:choose>
                                        <c:when test="${message.userId == userId}">
                                            <li>
                                                <div class="answer left">
                                                    <div class="avatar">
                                                        <img src="${message.photo}">
                                                    </div>
                                                    <div class="name">${message.nickname}</div>
                                                    <div class="text">${message.content}</div>
                                                </div>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>
                                                <div class="answer right">
                                                    <div class="avatar">
                                                        <img src="${message.photo}">
                                                    </div>
                                                    <div class="name">${message.nickname}</div>
                                                    <div class="text">${message.content}</div>
                                                </div>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </div>
        <div id="div" class="chat-body">
            <form id="messageForm" name="messageForm" nameForm="messageForm">
                <div class="answer-add" id="answer-add">
                    <textarea id="message" placeholder="Write a message"></textarea>
                    <input type="submit" class="answer-btn answer-btn-2" value="">
                </div>
            </form>
        </div>
    </div>
</div>

<script src="/js/main.js"></script>
<script src="/js/index.js"></script>
</body>
</html>