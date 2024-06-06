const audioDiv = document.getElementById('audioDiv');

const audio = new Audio();
audio.controls = false;


const ws = io('http://localhost:8085', {
    query: {
        room: roomName
    }
});

function sendMessage(room, command, value) {
    let message = {
        room: room,
        commandName: command,
        value: value
    };
    ws.emit('commands', message);
}

async function loadSong(id) {
    // AJAX request to get song by id
    const response = await fetch("http://localhost:8080/play/" + id);
    const data = await response.arrayBuffer();
    const blob = new Blob([data], { type: "audio/mpeg" });
    const blobUrl = URL.createObjectURL(blob);

    audio.src = blobUrl;
    audio.controls = true;
    audioDiv.innerHTML = '';
    audioDiv.appendChild(audio);

}

audio.addEventListener('volumechange', function () {
    sendMessage(roomName, 'VOLUME', audio.volume);
});

audio.addEventListener('seeking', function () {
    sendMessage(roomName, 'SEEK_TO', audio.currentTime);
});

audio.addEventListener('play', function () {
    sendMessage(roomName, 'PLAY', null);
});

audio.addEventListener('pause', function () {
    sendMessage(roomName, 'PAUSE', null);
});

ws.on('get_command', function (data) {

    if (data.commandName === 'OPEN') {
        loadSong(data.value);
    } else if (data.commandName === 'PAUSE') {
        audio.pause();
    } else if (data.commandName === 'SEEK_TO') {
        audio.currentTime = data.value;
    } else if (data.commandName === 'PLAY') {
        audio.play();
    } else if (data.commandName === 'STOP') {
        audio.pause();
        audio.currentTime = 0;
    } else if (data.commandName === 'VOLUME') {
        audio.volume = data.value;
    } else if (data.commandName === 'SYNC') {
        audio.currentTime = data.value;
    }

});
