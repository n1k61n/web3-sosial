import { useWeb3 } from '../context/Web3Context'
import { Navigate } from 'react-router-dom'

const Login = () => {
  const { account, connect, loading } = useWeb3()

  if (account) return <Navigate to="/" />

  return (
    <div className="min-h-screen flex items-center justify-center relative overflow-hidden">
      {/* Background effects */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-[#00ff88]/5 rounded-full blur-3xl"></div>
        <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-[#8b5cf6]/5 rounded-full blur-3xl"></div>
      </div>

      {/* Grid pattern */}
      <div className="absolute inset-0 opacity-5"
        style={{
          backgroundImage: 'linear-gradient(#00ff88 1px, transparent 1px), linear-gradient(90deg, #00ff88 1px, transparent 1px)',
          backgroundSize: '50px 50px'
        }}
      ></div>

      <div className="relative z-10 text-center max-w-md px-6 animate-fade-in">
        <div className="mb-8">
          <h1 className="font-display text-6xl font-bold glow-text mb-2">W3SOSIAL</h1>
          <p className="text-[#64748b] font-body text-lg">Desentralizə olunmuş sosial şəbəkə</p>
        </div>

        <div className="cyber-card p-8 mb-6">
          <div className="space-y-4 mb-8">
            {[
              { icon: '🔐', text: 'Wallet ilə giriş' },
              { icon: '⛓️', text: 'Blockchain əsaslı' },
              { icon: '🌐', text: 'IPFS saxlama' },
              { icon: '💎', text: 'Token mükafatları' },
            ].map((item, i) => (
              <div key={i} className="flex items-center gap-3 text-left">
                <span className="text-xl">{item.icon}</span>
                <span className="text-[#64748b] font-body text-sm">{item.text}</span>
              </div>
            ))}
          </div>

          <button
            onClick={connect}
            disabled={loading}
            className="cyber-btn w-full py-4 text-base disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? 'QOŞULUR...' : '🦊 METAMASK İLƏ GİRİŞ'}
          </button>
        </div>

        <p className="text-xs text-[#64748b] font-body">
          MetaMask olmadan giriş mümkün deyil
        </p>
      </div>
    </div>
  )
}

export default Login
