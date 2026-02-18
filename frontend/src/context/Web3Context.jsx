import { createContext, useContext, useState, useEffect } from 'react'
import { ethers } from 'ethers'
import axios from 'axios'

const Web3Context = createContext()

export const Web3Provider = ({ children }) => {
  const [account, setAccount] = useState(null)
  const [token, setToken] = useState(localStorage.getItem('token'))
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    const savedAccount = localStorage.getItem('account')
    if (savedAccount && token) {
      setAccount(savedAccount)
    }
  }, [])

const connect = async () => {
  // Edge-də ethereum gec gəlir, gözləyək
  let ethereum = window.ethereum

  if (!ethereum) {
    // 1 saniyə gözlə və yenidən yoxla
    await new Promise(resolve => setTimeout(resolve, 1000))
    ethereum = window.ethereum
  }

  if (!ethereum) {
    alert('MetaMask qurulu deyil!')
    return
  }

  setLoading(true)
  try {
    const provider = new ethers.BrowserProvider(ethereum)
      const accounts = await provider.send('eth_requestAccounts', [])
      const walletAddress = accounts[0]

      const nonceRes = await axios.get(`/api/auth/nonce/${walletAddress}`)
      const message = nonceRes.data

      const signer = await provider.getSigner()
      const signature = await signer.signMessage(message)

      const authRes = await axios.post('/api/auth/authenticate', {
        walletAddress,
        signature,
        message
      })

      const { token: newToken } = authRes.data
      localStorage.setItem('token', newToken)
      localStorage.setItem('account', walletAddress)
      setToken(newToken)
      setAccount(walletAddress)

      await axios.post(`/api/users/${walletAddress}`)
    } catch (err) {
      console.error('Bağlantı xətası:', err)
    } finally {
      setLoading(false)
    }
  }

  const disconnect = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('account')
    setAccount(null)
    setToken(null)
  }

  return (
    <Web3Context.Provider value={{ account, token, loading, connect, disconnect }}>
      {children}
    </Web3Context.Provider>
  )
}

export const useWeb3 = () => useContext(Web3Context)
