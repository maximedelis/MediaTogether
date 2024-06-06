let ws;
let music;
let musicPlayer = document.getElementById('music-player');

function connect() {
    ws = new WebSocket('ws://localhost:8085/websocket.io');
    ws.onmessage = function(data) {
        console.log(data);
    }
}

function disconnect() {
    ws.close();
}

function sendMessage(room, command, value) {
    let message = {
        room: room,
        command: command,
        value: value
    };
    ws.send(JSON.stringify(message));
}

function getSong(id) {
    // GET to  /api/files/{id}
    fetch(`/api/files/${id}`)
        .then(response => response.json())
        .then(data => {
            music = data;
        });
    musicPlayer.src = music;
    console.log(music);
}