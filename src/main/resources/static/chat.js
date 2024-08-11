let stompClient = null;
let username = '';

function connect() {
    const socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            console.log('Received message: ' + message.content);
            displayMessage(message);
        });
    });
}

function setUsername() {
    username = document.getElementById('username-input').value.trim();
    if (username) {
        document.getElementById('login-container').style.display = 'none';
        document.getElementById('chat-container').style.display = 'block';
        connect();
    } else {
        alert('Please enter a username.');
    }
}

window.setUsername = setUsername;

function sendMessage() {
    if (!username) {
        alert('Please set your username first.');
        return;
    }

    var messageInput = document.getElementById('message-input');
    var message = {
        sender: username,
        content: messageInput.value,
        timestamp: new Date().toISOString()
    };
    stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
    messageInput.value = '';
}

function formatTimestamp(timestamp) {
    const now = new Date();
    const messageDate = new Date(timestamp);
    const diffDays = Math.floor((now - messageDate) / (1000 * 60 * 60 * 24));

    let dateStr;

    if (diffDays === 0) {
        dateStr = 'Today';
    } else if (diffDays === 1) {
        dateStr = 'Yesterday';
    } else {
        // Формат даты как "DD/MM/YYYY"
        const day = String(messageDate.getDate()).padStart(2, '0');
        const month = String(messageDate.getMonth() + 1).padStart(2, '0');
        const year = messageDate.getFullYear();
        dateStr = `${day}/${month}/${year}`;
    }

    // Формат времени без секунд
    const hours = String(messageDate.getHours()).padStart(2, '0');
    const minutes = String(messageDate.getMinutes()).padStart(2, '0');
    const timeStr = `${hours}:${minutes}`;

    return `${dateStr} ${timeStr}`;
}

let secretKey = ''; // Клиентский секретный ключ (должен быть синхронизирован с сервером)

function encryptMessage(message) {
    // Шифрование сообщения на стороне клиента
    // Использование аналогичной логики как на сервере (например, библиотека CryptoJS для AES)
    // Здесь нужно добавить код для шифрования с использованием секретного ключа
    return message; // Зашифрованное сообщение
}

function decryptMessage(encryptedMessage) {
    // Дешифрование сообщения на стороне клиента
    // Использование аналогичной логики как на сервере
    return encryptedMessage; // Расшифрованное сообщение
}


window.sendMessage = async function () {
    if (!username) {
        alert('Please set your username first.');
        return;
    }

    var messageInput = document.getElementById('message-input');
    var fileInput = document.getElementById('file-input');
    var messageContent = messageInput.value.trim();
    var fileInfo = null;

    // Если выбран файл, загружаем его на сервер
    if (fileInput.files.length > 0) {
        var formData = new FormData();
        formData.append("file", fileInput.files[0]);
        formData.append("sender", username);

        let response = await fetch('/api/chat/files/upload', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            let fileId = await response.text();
            console.log('File uploaded successfully with ID:', fileId);
            fileInfo = {
                fileId: fileId,
                fileName: fileInput.files[0].name
            };
        } else {
            alert('File upload failed.');
            return;
        }

        fileInput.value = '';
    }

    // Создаем сообщение с текстом и (если есть) информацией о файле
    var message = {
        sender: username,
        content: messageContent || (fileInfo ? `File uploaded: ${fileInfo.fileName}` : ''),
        timestamp: new Date().toISOString(),
        fileId: fileInfo ? fileInfo.fileId : null,
        fileName: fileInfo ? fileInfo.fileName : null
    };

    console.log('Message being sent:', message);

    stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
    messageInput.value = '';
};





function displayMessage(message) {
    var messagesDiv = document.getElementById('messages');
    var messageDiv = document.createElement('div');
    messageDiv.className = 'message';

    var contentDiv = document.createElement('div');
    contentDiv.className = 'message-content';

    if (message.fileId) {
        // Создаем ссылку на файл
        var fileLink = document.createElement('a');
        fileLink.href = `/api/chat/files/${message.fileId}`; // Путь к файлу
        fileLink.textContent = message.fileName || 'file'; // Имя файла
        fileLink.setAttribute('download', message.fileName); // Предлагает имя файла для скачивания

        // Формируем содержимое сообщения
        contentDiv.innerHTML = `${message.sender}: File uploaded: <a href="${fileLink.href}" download>${fileLink.textContent}</a>`;
    } else {
        // Отображаем просто текст сообщения
        contentDiv.textContent = `${message.sender}: ${message.content}`;
    }

    messageDiv.appendChild(contentDiv);

    // Отображаем метку времени
    var timestampDiv = document.createElement('div');
    timestampDiv.className = 'message-timestamp';
    timestampDiv.textContent = formatTimestamp(message.timestamp);
    messageDiv.appendChild(timestampDiv);

    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}
