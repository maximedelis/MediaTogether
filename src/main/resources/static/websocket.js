const audioDiv = document.getElementById('audioDiv');

const audio = new Audio();
audio.controls = false;
audio.id = 'audio';

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
    const response = await fetch("http://localhost:8080/play/" + id);
    const data = await response.arrayBuffer();
    const blob = new Blob([data], { type: "audio/mpeg" });
    const blobUrl = URL.createObjectURL(blob);

    audio.src = blobUrl;
    audio.controls = true;
    audioDiv.innerHTML = '';
    audioDiv.appendChild(audio);
    //onAudioCreated();

    // Add stop button
    const stopButton = document.createElement('button');
    stopButton.innerHTML = 'Stop';
    stopButton.onclick = function () {
        audio.pause();
        audio.currentTime = 0;
        sendMessage(roomName, 'STOP', null);
    }
    audioDiv.appendChild(stopButton);
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


function sendSeekTo() {
    sendMessage(roomName, 'SEEK_TO', audio.currentTime);
}

//setInterval(sendSeekTo, 1000);

function fastAddEventListener(element, type, listener) {
    element.addEventListener(type, listener);
}

function fastRemoveEventListener(element, type, listener) {
    element.removeEventListener(type, listener);
}

audio.addEventListener('seeking', sendSeekTo);

ws.on('get_command', function (data) {

    if (data.commandName === 'OPEN') {
        loadSong(data.value);
    } else if (data.commandName === 'PAUSE') {
        audio.pause();
    } else if (data.commandName === 'SEEK_TO') {
        fastRemoveEventListener(audio, 'seeking', sendSeekTo);
        audio.currentTime = data.value;
        fastAddEventListener(audio, 'seeking', sendSeekTo);
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


// get button with id x and add event listener
for (let i = 1; i < sizeSongs+1; i++) {
    console.log(i);
    const button = document.getElementById(i);
    button.addEventListener('click', function () {
        loadSong(i);
        sendMessage(roomName, 'OPEN', i);
    });
}

/*
function onAudioCreated() {
    const audioPlayer = document.getElementById("audio");
    const playPauseButton = document.getElementById("play-pause-button");
    const volumeControl = document.getElementById("volume-control");
    const progressBar = document.getElementById("progress-bar");
    const currentTimeDisplay = document.getElementById("current-time");
    const totalTimeDisplay = document.getElementById("total-time");

    let isPlaying = false;

    playPauseButton.addEventListener("click", () => {
        if (isPlaying) {
            audioPlayer.pause();
            playPauseButton.textContent = "Play";
        } else {
            audioPlayer.play();
            playPauseButton.textContent = "Pause";
        }
        isPlaying = !isPlaying;
    });

    volumeControl.addEventListener("input", () => {
        audioPlayer.volume = volumeControl.value;
    });

    audioPlayer.addEventListener("timeupdate", () => {
        const currentTime = audioPlayer.currentTime;
        const duration = audioPlayer.duration;

        const currentMinutes = Math.floor(currentTime / 60);
        const currentSeconds = Math.floor(currentTime % 60);
        const totalMinutes = Math.floor(duration / 60);
        const totalSeconds = Math.floor(duration % 60);

        currentTimeDisplay.textContent = `${currentMinutes}:${currentSeconds < 10 ? '0' : ''}${currentSeconds}`;
        totalTimeDisplay.textContent = `${totalMinutes}:${totalSeconds < 10 ? '0' : ''}${totalSeconds}`;

        const progress = (currentTime / duration) * 100;
        progressBar.style.width = `${progress}%`;
    });
}
*/
