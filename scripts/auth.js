const loginForm = document.querySelector(".login form");
const signUpForm = document.querySelector(".signup form");
const checkbox = document.querySelector('input[type="checkbox"]');

const BASE_URL = "http://localhost:8080";

loginForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = e.target["username"].value;
  const password = e.target["password"].value;

  const response = await fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username,
      password,
    }),
  });
  e.target.reset();

  const result = await response.json();

  localStorage.setItem("token", result.token);
  window.location.href = "/";

  return result;
});

signUpForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = e.target["username"].value;
  const password = e.target["password"].value;

  const response = await fetch(`${BASE_URL}/auth/signup`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username,
      password,
    }),
  });
  e.target.reset();

  const result = await response.json();
  checkbox.click();

  return result;
});
