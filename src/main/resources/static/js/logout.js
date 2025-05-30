function logout() {
  const confirmed = confirm("本当にログアウトしますか？");

  if (!confirmed) return;

  fetch("/api/auth/logout", {
    method: "POST",
  }).then(() => {
    location.href = "/login";
  });
}
