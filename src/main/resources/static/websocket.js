const audioDiv = document.getElementById('audioDiv');

const audio = new Audio();
audio.controls = false;
audio.id = 'audio';

const SYNC_INTERVAL = 3000;
const currentTimeInput = document.getElementById('current_time');

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
    ws.emit('command', message);
}

async function loadSong(id) {
    const response = await fetch("http://localhost:8080/play/" + id);
    const data = await response.arrayBuffer();
    const blob = new Blob([data], { type: "audio/mpeg" });
    const blobUrl = URL.createObjectURL(blob);

    audio.src = blobUrl;
    audio.controls = true;
    audioDiv.innerHTML = '';
    audioDiv.appendChild(audio);

    const stopButton = document.createElement('button');
    stopButton.innerHTML = 'Stop';
    stopButton.onclick = function () {
        audio.pause();
        audio.currentTime = 0;
        currentTimeInput.value = 0;
        sendMessage(roomName, 'STOP', null);
    }
    audioDiv.appendChild(stopButton);
}

function fastAddEventListener(element, type, listener) {
    element.addEventListener(type, listener);
}

function fastRemoveEventListener(element, type, listener) {
    element.removeEventListener(type, listener);
}

audio.addEventListener('volumechange', function () {
    sendMessage(roomName, 'VOLUME', audio.volume);
});

audio.addEventListener('mute', function () {
    sendMessage(roomName, 'VOLUME', 0);
});

audio.addEventListener('play', function () {
    sendMessage(roomName, 'PLAY', null);
});

audio.addEventListener('pause', function () {
    sendMessage(roomName, 'PAUSE', null);
});

function syncCurrentTime() {
    currentTimeInput.value = 100 * audio.currentTime / audio.duration;
}

fastAddEventListener(audio, 'timeupdate', syncCurrentTime);

currentTimeInput.addEventListener('mousedown', function () {
    fastRemoveEventListener(audio, 'timeupdate', syncCurrentTime);
});

currentTimeInput.addEventListener('change', function () {
    sendMessage(roomName, 'SEEK_TO', 0.01 * currentTimeInput.value * audio.duration);
    audio.currentTime = 0.01 * currentTimeInput.value * audio.duration;
    fastAddEventListener(audio, 'timeupdate', syncCurrentTime);
});


for (let i = 1; i < sizeSongs+1; i++) {
    const button = document.getElementById(i);
    button.addEventListener('click', function () {
        loadSong(i);
        sendMessage(roomName, 'OPEN', i);
    });
}

ws.on('get_command', function (data) {
    if (data.commandName === 'OPEN') {
        loadSong(data.value);
    } else if (data.commandName === 'PAUSE') {
        audio.pause();
    } else if (data.commandName === 'SEEK_TO') {
        audio.currentTime = data.value;
        currentTimeInput.value = data.value / (0.01 * audio.duration);
    } else if (data.commandName === 'PLAY') {
        audio.play();
    } else if (data.commandName === 'STOP') {
        audio.pause();
        audio.currentTime = 0;
        currentTimeInput.value = 0;
    } else if (data.commandName === 'VOLUME') {
        audio.volume = data.value;
    } else if (data.commandName === 'SYNC') {
        audio.currentTime = data.value;
    }
});
