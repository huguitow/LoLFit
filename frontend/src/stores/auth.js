import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
    const token = ref(localStorage.getItem('token'))
    const isAuthenticated = ref(token.value !== null)

    const setToken = (newToken) => {
        token.value = newToken
        isAuthenticated.value = true
        localStorage.setItem('token', token.value)
    }

    const logout = () => {
        token.value = null
        isAuthenticated.value = false
        localStorage.removeItem('token')
    }

    return {
        token,
        isAuthenticated,
        setToken,
        logout
    }
})