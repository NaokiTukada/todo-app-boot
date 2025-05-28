document.addEventListener("DOMContentLoaded", function () {
    const userIdInput = document.getElementById("userId");
    if (!userIdInput) return;

    const userId = userIdInput.value;

    fetch(`/tasks/user/${userId}/updateStreak`, {
        method: "POST"
    })
    .then(response => {
        if (!response.ok) throw new Error("ストリーク更新に失敗しました");
        return response.text();
    })
    .then(data => {
        console.log("ストリーク更新成功:", data);
    })
    .catch(error => {
        console.error("エラー:", error);
    });
});
