import { useQuery } from '@tanstack/react-query'
import { useWeb3 } from '../context/Web3Context'
import { notificationService } from '../services/api'
import { Navigate } from 'react-router-dom'

const typeIcon = {
  LIKE: '♥',
  COMMENT: '💬',
  FOLLOW: '👤',
  REPOST: '🔁',
  MENTION: '@',
}

const typeColor = {
  LIKE: 'text-red-400',
  COMMENT: 'text-[#8b5cf6]',
  FOLLOW: 'text-[#00ff88]',
  REPOST: 'text-[#3b82f6]',
  MENTION: 'text-yellow-400',
}

const Notifications = () => {
  const { account } = useWeb3()

  if (!account) return <Navigate to="/login" />

  const { data, isLoading, refetch } = useQuery({
    queryKey: ['notifications', account],
    queryFn: () => notificationService.getAll(account).then(r => r.data)
  })

  const handleMarkAll = async () => {
    await notificationService.markAllAsRead(account)
    refetch()
  }

  const handleMarkOne = async (id) => {
    await notificationService.markAsRead(id)
    refetch()
  }

  const formatDate = (date) => {
    return new Date(date).toLocaleDateString('az-AZ', {
      day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit'
    })
  }

  const shortAddress = (addr) =>
    addr ? `${addr.slice(0, 6)}...${addr.slice(-4)}` : ''

  return (
    <div className="max-w-2xl mx-auto px-4 py-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="font-display text-xl glow-text">BİLDİRİŞLƏR</h1>
        {data?.some(n => !n.isRead) && (
          <button onClick={handleMarkAll} className="cyber-btn-outline text-xs">
            HAMISI OXUNDU
          </button>
        )}
      </div>

      {isLoading ? (
        <div className="space-y-3">
          {[1, 2, 3].map(i => (
            <div key={i} className="cyber-card p-4 animate-pulse">
              <div className="h-3 bg-[#1e1e2e] rounded w-3/4"></div>
            </div>
          ))}
        </div>
      ) : data?.length === 0 ? (
        <div className="text-center py-16 text-[#64748b]">
          <p className="font-display text-4xl mb-4">🔔</p>
          <p className="font-display">Bildiriş yoxdur</p>
        </div>
      ) : (
        <div className="space-y-3">
          {data?.map(n => (
            <div
              key={n.id}
              onClick={() => !n.isRead && handleMarkOne(n.id)}
              className={`cyber-card p-4 cursor-pointer transition-colors hover:border-[#2e2e4e] ${
                !n.isRead ? 'border-[#1e1e3e] bg-[#111120]' : ''
              }`}
            >
              <div className="flex items-start gap-3">
                <span className={`text-xl ${typeColor[n.type]}`}>
                  {typeIcon[n.type]}
                </span>
                <div className="flex-1">
                  <p className="text-sm text-[#e2e8f0] font-body">{n.message}</p>
                  <div className="flex items-center gap-2 mt-1">
                    <span className="font-display text-xs text-[#64748b]">{shortAddress(n.senderAddress)}</span>
                    <span className="text-[#64748b]">·</span>
                    <span className="text-xs text-[#64748b]">{formatDate(n.createdAt)}</span>
                  </div>
                </div>
                {!n.isRead && (
                  <div className="w-2 h-2 rounded-full bg-[#00ff88] mt-1 animate-glow flex-shrink-0"></div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default Notifications
