let ws;

function connect() {
    ws = new WebSocket('ws://localhost:8085/websocket.io');
    ws.onmessage = function(data) {
        console.log(data);
    }
}

function disconnect() {
    ws.close();
}

function sendMessage() {
    ws.send('Hello from client');
}