### WebSocket方案汇总

##### 方案1：Html5（WebSocket） + Netty;
* Client：Html5（WebSocket）；
* Server：[netty](https://github.com/netty/netty)

##### 方案2：Socket.io + Netty-socketio;
* Client：[socket.io](http://socket.io/) 
* Server：[netty-socketio](https://github.com/mrniko/netty-socketio)
* DEMO：[netty-socketio-demo](https://github.com/mrniko/netty-socketio-demo)

> 简介
1. **Socket.io**：封装了websocket，包括client、server端，此处作为client使用；

Socket.IO 实现了实时双向的基于事件的通讯机制。旨在让各种浏览器与移动设备上实现实时app功能，模糊化各种传输机制。

socket.io封装了websocket，同时包含了其它的连接方式，比如Ajax。原因在于不是所有的浏览器都支持websocket，通过socket.io的封装，你不用关心里面用了什么连接方式。你在任何浏览器里都可以使用socket.io来建立异步的连接。socket.io包含了服务端和客户端的库，如果在浏览器中使用了socket.io的js，服务端也必须同样适用。如果你很清楚你需要的就是websocket，那可以直接使用websocket。

2. **netty-socketio**：封装了socket.io，此处作为server使用；这是一个Socket.IO服务器端实现，基于netty框架，适合于 socket.io 0.9-1.0版本（虽然socket.io 目前还处于0.9版本，会支持到1.0）；

> Tips：IE、火狐都OK，360谷歌内核经常有问题，setHost.../setPort...即可解决；（socket-netty兼容性很好，支持各种协议，但是貌似不更新了）。之前有一款产品“socketio-netty”兼容性更好，不过貌似不更新了，可惜；

> 实现聊天室：socket.io-1.7.6 + netty-socketio-0.9.16，源码可查看个人站项目；