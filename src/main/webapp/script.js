// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

async function loadPosts() {
    
    // Get response from display-posts server 
    const responseFromServer = await fetch('/display-posts');
    const jsonFromResponse = await responseFromServer.json();
    console.log(jsonFromResponse);

    // Create the html container for the posts
    const displayPostsContainer = document.getElementById('display-posts-container');

    // Loop through all posts in the server response and create a div for each one
    jsonFromResponse.forEach(postContent => {
        displayPostsContainer.appendChild(createPostElement(postContent));
    });
}

// Takes in a string and returns a div element containing the string
function createPostElement(post) {
    const postElement = document.createElement('div');
    postElement.innerText = post;
    return postElement;
}