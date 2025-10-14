import React from 'react'
import LoginContainer from '../../components/LoginContainer/LoginContainer.jsx'

export default function Login() {
  return (
    <div>
        <LoginContainer onLogin={() => {
            // Handle login logic here
            console.log("Login button clicked");
        }} />
    </div>
  )
}
