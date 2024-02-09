//Display Tasks
function fetchAndDisplayTasks(endpoint) {
  fetch(`${endpoint}`)
    .then((response) => response.json())
    .then((tasks) => {
      const taskList = document.getElementById("task-list");
      taskList.innerHTML = "";
      let numOfTasks = 0;

      tasks.forEach((task) => {
        const taskItem = document.createElement("li");
        taskItem.classList.add("list-item");
        task.completed
          ? taskItem.classList.add("completed")
          : taskItem.classList.add("active");
        taskItem.innerHTML = `
            <div>
            <div class="checkbox-container">
            <input type="checkbox" id="task-status-${task.id}" ${
          task.completed ? "checked" : ""
        }/>
            <label for="task-status-${task.id}" class="round-checkbox"></label>
            </div>
            ${task.description}
            </div>
            <button id="delete-btn-${task.id}" class="delete-btn">
            <img
            src="./images/icon-cross.svg"
            alt="delete-icon"
            class="delete-icon"
          /></button>`;
        taskItem.id = task.id;
        taskList.appendChild(taskItem);
        document
          .getElementById(`task-status-${taskItem.id}`)
          .addEventListener("click", function () {
            const taskId = taskItem.id;
            updateTaskStatus(taskId);
          });
        document
          .getElementById(`delete-btn-${taskItem.id}`)
          .addEventListener("click", function () {
            const taskId = task.id;
            deleteTask(taskId);
          });
        if (!task.completed) {
          numOfTasks++;
        }
      });
      document.getElementById("taskcount").textContent = `${numOfTasks} ${
        numOfTasks > 1 ? " items left" : " item left"
      }`;
    })
    .catch((error) => console.error("Error fetching tasks:", error));
}

// Add new task
function addTask(taskInput, status = false) {
  fetch("/api/tasks", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ description: taskInput, completed: status }),
  })
    .then((response) => response.json())
    .then((newTask) => {
      const taskList = document.getElementById("task-list");

      const taskItem = document.createElement("li");
      taskItem.textContent = newTask.description;
      taskList.appendChild(taskItem);
      fetchAndDisplayTasks("/api/tasks");
    })
    .catch((error) => console.error("Error adding task:", error));
}

// Delete task
function deleteTask(taskId) {
  fetch(`/api/tasks/${taskId}`, {
    method: "DELETE",
  })
    .then(() => {
      fetchAndDisplayTasks("/api/tasks");
    })
    .catch((error) => console.error("Error deleting the task: ", error));
}

// Update status of task
function updateTaskStatus(taskId) {
  fetch(`/api/tasks/${taskId}`, {
    method: "PATCH",
  })
    .then(() => {
      if (
        document.getElementById("completed-btn").classList.contains("current")
      ) {
        fetchAndDisplayTasks("/api/tasks/completed");
      } else if (
        document.getElementById("active-btn").classList.contains("current")
      ) {
        fetchAndDisplayTasks("/api/tasks/active");
      } else {
        fetchAndDisplayTasks("/api/tasks");
      }
    })
    .catch((error) =>
      console.error("Error updating the status of the task: ", error)
    );
}

//Delete completed tasks and display only active tasks
document.getElementById("clear-btn").addEventListener("click", function () {
  fetch("/api/tasks/completed", {
    method: "DELETE",
  })
    .then(() => {
      fetchAndDisplayTasks("/api/tasks");
    })
    .catch((error) => console.error("Error adding task:", error));
});

// Add event listener for the form submission
document.getElementById("task-form").addEventListener("keydown", function (e) {
  if (e.key === "Enter") {
    e.preventDefault();

    const taskInput = document.getElementById("task-input").value;
    const status = document.getElementById("new-task").checked ? true : false;
    if (taskInput.trim() !== "") {
      addTask(taskInput, status);
    }

    document.getElementById("task-input").value = "";
  }
});

// Fetch completed tasks when completed button is pressed
document.getElementById("completed-btn").addEventListener("click", function () {
  fetchAndDisplayTasks("/api/tasks/completed");

  document.getElementById("completed-btn").classList.add("current");
  document.getElementById("active-btn").classList.remove("current");
  document.getElementById("all-btn").classList.remove("current");
});

// Fetch and display active tasks when active button is pressed
document.getElementById("active-btn").addEventListener("click", function () {
  fetchAndDisplayTasks("/api/tasks/active");

  document.getElementById("completed-btn").classList.remove("current");
  document.getElementById("active-btn").classList.add("current");
  document.getElementById("all-btn").classList.remove("current");
});

// Fetch and display all tasks when all button is pressed
document.getElementById("all-btn").addEventListener("click", function () {
  fetchAndDisplayTasks("/api/tasks");

  document.getElementById("completed-btn").classList.remove("current");
  document.getElementById("active-btn").classList.remove("current");
  document.getElementById("all-btn").classList.add("current");
});

// Fetch and display tasks when the page loads
document.addEventListener("DOMContentLoaded", function () {
  fetchAndDisplayTasks("/api/tasks");
});

//Change the mode of the app
document.getElementById("mode-icon").addEventListener("click", function () {
  const root = document.documentElement;
  const modeImg = document.getElementById("mode-icon");
  const header = document.getElementById("header");
  const currentWidth = window.innerWidth;
  let newCSSVariables;
  let imgSrc;
  let imgAlt;
  let headerImg;

  //Set new variables depending on current mode
  if (modeImg.classList.contains("dark")) {
    newCSSVariables = {
      "--mainBackgroundColor": "hsl(236, 33%, 92%)",
      "--taskBackgroundColor": "hsl(0, 0%, 98%)",
      "--taskColorActive": "hsl(236, 9%, 61%)",
      "--taskColorComplete": "hsl(233, 11%, 84%)",
      "--hover": "hsl(235, 19%, 35%)",
    };

    imgSrc = "images/icon-moon.svg";
    imgAlt = "moon-icon";
    if (currentWidth >= 768) {
      headerImg = "background-image: url('./images/bg-desktop-light.jpg'";
    } else {
      headerImg = "background-image: url('./images/bg-mobile-light.jpg'";
    }
  } else {
    newCSSVariables = {
      "--mainBackgroundColor": "hsl(235, 21%, 11%)",
      "--taskBackgroundColor": "hsl(235, 24%, 19%)",
      "--taskColorActive": "hsl(234, 39%, 85%)",
      "--taskColorComplete": "hsl(233, 14%, 35%)",
      "--hover": "hsl(236, 33%, 92%)",
    };

    imgSrc = "images/icon-sun.svg";
    imgAlt = "sun-icon";
    if (currentWidth >= 768) {
      headerImg = "background-image: url('./images/bg-desktop-dark.jpg'";
    } else {
      headerImg = "background-image: url('./images/bg-mobile-dark.jpg'";
    }
  }

  //Change css variables
  for (const [variable, value] of Object.entries(newCSSVariables)) {
    root.style.setProperty(variable, value);
  }

  //Change image source
  modeImg.src = imgSrc;
  modeImg.alt = imgAlt;

  //Update class
  modeImg.classList.toggle("dark");

  //Update background image
  header.setAttribute("style", headerImg);
});
