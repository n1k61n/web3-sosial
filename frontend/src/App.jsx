import { Routes, Route, Navigate } from 'react-router-dom'
import { Web3Provider } from './context/Web3Context'
import Navbar from './components/Navbar'
import Login from './pages/Login'
import Feed from './pages/Feed'
import Profile from './pages/Profile'
import Notifications from './pages/Notifications'

function App() {
  return (
    <Web3Provider>
      <div className="min-h-screen bg-[#0a0a0f]">
        <Navbar />
        <main className="pt-16">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/" element={<Feed />} />
            <Route path="/profile/:wallet" element={<Profile />} />
            <Route path="/notifications" element={<Notifications />} />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </main>
      </div>
    </Web3Provider>
  )
}

export default App
