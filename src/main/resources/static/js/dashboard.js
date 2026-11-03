document.addEventListener('DOMContentLoaded', function () {
    const compilerLanguage = document.getElementById('compilerLanguage');
    const compilerFrame = document.getElementById('compilerFrame');
    const videoUrlInput = document.querySelector('input[name="videoUrl"]');
    const loadVideoBtn = document.getElementById('loadVideoBtn');
    const youtubeIframe = document.getElementById('youtubePlayer');
    const videoPlaceholder = document.getElementById('videoPlaceholder');
    const videoLoadMessage = document.getElementById('videoLoadMessage');
    const noteTitleInput = document.querySelector('input[name="title"]');
    const noteContentInput = document.querySelector('textarea[name="content"]');

    if (compilerLanguage && compilerFrame) {
        compilerLanguage.addEventListener('change', function () {
            const selectedOption = compilerLanguage.options[compilerLanguage.selectedIndex];
            compilerFrame.src = selectedOption.value;
            compilerFrame.title = `${selectedOption.text} Online Compiler`;
        });
    }

    if (loadVideoBtn && videoUrlInput && youtubeIframe && videoPlaceholder && videoLoadMessage) {
        loadVideoBtn.addEventListener('click', function () {
            const match = videoUrlInput.value.trim().match(
                /(?:https?:\/\/)?(?:www\.|m\.)?(?:youtube\.com\/(?:watch\?v=|shorts\/|live\/)|youtu\.be\/)([A-Za-z0-9_-]{11})/
            );

            if (!match) {
                videoLoadMessage.textContent = 'Enter a valid YouTube video URL.';
                videoLoadMessage.classList.add('text-danger');
                videoLoadMessage.classList.remove('text-success');
                return;
            }

            youtubeIframe.src = `https://www.youtube.com/embed/${match[1]}?autoplay=1&playsinline=1`;
            youtubeIframe.classList.remove('d-none');
            videoPlaceholder.classList.add('d-none');
            videoLoadMessage.textContent = 'Video loaded. Press play in the player to start watching.';
            videoLoadMessage.classList.add('text-success');
            videoLoadMessage.classList.remove('text-danger');
        });
    }

    const newNoteBtn = document.getElementById('newNoteBtn');
    if (newNoteBtn && noteTitleInput && noteContentInput) {
        newNoteBtn.addEventListener('click', function () {
            noteTitleInput.value = '';
            noteContentInput.value = '';
        });
    }
});
