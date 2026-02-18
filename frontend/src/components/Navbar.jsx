import { Link, useLocation } from 'react-router-dom'
import { useWeb3 } from '../context/Web3Context'
import { useState, useEffect } from 'react'
import { notificationService } from '../services/api'

const Navbar = () => {
  const { account, disconnect } = useWeb3()
  const location = useLocation()
  const [unreadCount, setUnreadCount] = useState(0)

  useEffect(() => {
    if (account) {
      notificationService.getCount(account)
        .then(res => setUnreadCount(res.data))
        .catch(() => {})
    }
  }, [account, location])

  const shortAddress = (addr) =>
    addr ? `${addr.slice(0, 6)}...${addr.slice(-4)}` : ''

  return (
    <nav className="fixed top-0 left-0 right-0 z-50 border-b border-[#1e1e2e] bg-[#0a0a0f]/90 backdrop-blur-md">
      <div className="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
        <Link to="/" className="font-display text-xl font-bold glow-text">
          W3SOSIAL
        </Link>

        {account && (
          <div className="flex items-center gap-6">
            <Link
              to="/"
              className={`text-sm font-display ${location.pathname === '/' ? 'text-[#00ff88]' : 'text-[#64748b] hover:text-white'} transition-colors`}
            >
              FEED
            </Link>
            <Link
              to={`/profile/${account}`}
              className={`text-sm font-display ${location.pathname.includes('/profile') ? 'text-[#00ff88]' : 'text-[#64748b] hover:text-white'} transition-colors`}
            >
              PROFİL
            </Link>
            <Link
              to="/notifications"
              className={`text-sm font-display relative ${location.pathname === '/notifications' ? 'text-[#00ff88]' : 'text-[#64748b] hover:text-white'} transition-colors`}
            >
              BİLDİRİŞ
              {unreadCount > 0 && (
                <span className="absolute -top-2 -right-3 bg-[#00ff88] text-[#0a0a0f] text-xs rounded-full w-4 h-4 flex items-center justify-center font-bold">
                  {unreadCount}
                </span>
              )}
            </Link>
          </div>
        )}

        {account ? (
          <div className="flex items-center gap-3">
            <span className="font-display text-xs text-[#64748b] border border-[#1e1e2e] px-3 py-1.5 rounded-lg">
              {shortAddress(account)}
            </span>
            <button onClick={disconnect} className="cyber-btn-outline text-xs px-3 py-1.5">
              ÇIXIŞ
            </button>
          </div>
        ) : null}
      </div>
    </nav>
  )
}

export default Navbar
