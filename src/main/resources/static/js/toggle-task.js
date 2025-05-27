function toggleTaskCompletion(taskId) {
    fetch(`/tasks/${taskId}/toggle`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('完了状態の切り替えに失敗しました。');
        }
        location.reload();
    })
    .catch(error => {
        console.error('エラー:', error);
        alert(error.message);
    });
}
