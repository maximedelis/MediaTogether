const audioDiv = document.getElementById('audioDiv');

const audio = new Audio();
audio.controls = false;
audio.id = 'audio';
audio.hidden = true;

const SYNC_INTERVAL = 3000;
const currentTimeInput = document.getElementById('current-time-input');
const volumeControlInput = document.getElementById('volume-control');
const divCurrentTime = document.getElementById('current-time');
const divTotalTime = document.getElementById('total-time');
const divControls = document.getElementById('controls');

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
    audioDiv.innerHTML = '';
    audioDiv.appendChild(audio);

    const stopButton = document.createElement('button');
    stopButton.innerHTML = 'Stop';
    stopButton.id = 'stop';
    stopButton.onclick = function () {
        audio.pause();
        audio.currentTime = 0;
        currentTimeInput.value = 0;
        sendMessage(roomName, 'STOP', null);
    }

    divControls.appendChild(stopButton);
    divControls.hidden = false;
}

document.querySelectorAll('button').forEach(button => {
    button.addEventListener('click', function () {
        sendMessage(roomName, 'OPEN', button.id);
        loadSong(button.id);
    });
});

function setTotalTime() {
    const duration = audio.duration;
    const totalMinutes = Math.floor(duration / 60);
    const totalSeconds = Math.floor(duration % 60);
    divTotalTime.textContent = `${totalMinutes}:${totalSeconds < 10 ? '0' : ''}${totalSeconds}`;
    currentTimeInput.value = 0;
}

const playPauseButton = document.createElement('button');
playPauseButton.id = 'play-pause';
playPauseButton.innerHTML = 'Play';
playPauseButton.onclick = function () {
    if (audio.paused) {
        audio.play();
    } else {
        audio.pause();
    }
}

divControls.appendChild(playPauseButton);

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
    playPauseButton.innerHTML = 'Pause';
});

audio.addEventListener('pause', function () {
    sendMessage(roomName, 'PAUSE', null);
    playPauseButton.innerHTML = 'Play';
});

audio.addEventListener('timeupdate', function () {
    const currentTime = audio.currentTime;

    const currentMinutes = Math.floor(currentTime / 60);
    const currentSeconds = Math.floor(currentTime % 60);

    divCurrentTime.textContent = `${currentMinutes}:${currentSeconds < 10 ? '0' : ''}${currentSeconds}`;
});

volumeControlInput.addEventListener('input', function () {
    audio.volume = volumeControlInput.value;
});

function syncCurrentTime() {
    currentTimeInput.value = 100 * audio.currentTime / audio.duration;
}

fastAddEventListener(audio, 'loadedmetadata', setTotalTime);
fastAddEventListener(audio, 'timeupdate', syncCurrentTime);

currentTimeInput.addEventListener('mousedown', function () {
    fastRemoveEventListener(audio, 'timeupdate', syncCurrentTime);
});

currentTimeInput.addEventListener('change', function () {
    sendMessage(roomName, 'SEEK_TO', 0.01 * currentTimeInput.value * audio.duration);
    audio.currentTime = 0.01 * currentTimeInput.value * audio.duration;
    fastAddEventListener(audio, 'timeupdate', syncCurrentTime);
});

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
        volumeControlInput.value = data.value;
        audio.volume = data.value;
    } else if (data.commandName === 'SYNC') {
        audio.currentTime = data.value;
    }
});
