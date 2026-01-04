import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './style.css'
import App from './App.tsx'

const container = document.getElementById('app')!
const root = createRoot(container)

root.render(
  <StrictMode>
    <App />
  </StrictMode>,
)
