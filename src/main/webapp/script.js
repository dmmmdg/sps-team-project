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
 * Adds the sign in with Google function.
 */
function handleCredentialResponse(response) {
      const data = jwt_decode(response.credential)
      console.log(data)
      document.getElementById('fullName').textContent = data.name
      document.getElementById('sub').textContent = data.sub
      document.getElementById('given_name').textContent = data.given_name
      document.getElementById('family_name').textContent = data.family_name
      document.getElementById('email').textContent = data.email
      document.getElementById('verifiedEmail').textContent = data.email_verified
      document.getElementById('picture').setAttribute("src", data.picture)
  }
window.onload = function () {
    google.accounts.id.initialize({
        client_id: "891385536925-sblb9nv3ehfc45p806jkkarnvjivb9he.apps.googleusercontent.com",
        callback: handleCredentialResponse
    });
    google.accounts.id.renderButton(
        document.getElementById("buttonDiv"),
        { theme: "outline", size: "large" }  // customization attributes
    );
    google.accounts.id.prompt(); // also display the One Tap dialog
}

