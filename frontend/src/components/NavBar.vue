<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const handleLogout = () => {
    authStore.logout()
    router.push('/login')
}
</script>

<template>
    <nav class="navbar">
        <div class="navbar-left">
            <router-link to="/champions" class="brand">LoLFit</router-link>
            <div class="nav-links">
                <router-link to="/champions">Champions</router-link>
                <router-link to="/wardrobe">Ma Garde-Robe</router-link>
            </div>
        </div>
        <div class="navbar-right">
            <button v-if="authStore.isAuthenticated" @click="handleLogout" class="btn-logout">
                Déconnexion
            </button>
            <router-link v-else to="/login" class="btn-login">Connexion</router-link>
        </div>
    </nav>
</template>

<style scoped>
.navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    height: 60px;
    background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.navbar-left {
    display: flex;
    align-items: center;
    gap: 32px;
}

.brand {
    font-size: 1.5rem;
    font-weight: 700;
    color: #c89b3c;
    text-decoration: none;
    letter-spacing: 1px;
}

.brand:hover {
    color: #e4c274;
}

.nav-links {
    display: flex;
    gap: 24px;
}

.nav-links a {
    color: #a0a0a0;
    text-decoration: none;
    font-weight: 500;
    transition: color 0.2s;
}

.nav-links a:hover,
.nav-links a.router-link-active {
    color: #ffffff;
}

.navbar-right {
    display: flex;
    align-items: center;
}

.btn-login {
    color: #c89b3c;
    text-decoration: none;
    font-weight: 500;
    padding: 8px 16px;
    border: 1px solid #c89b3c;
    border-radius: 4px;
    transition: all 0.2s;
}

.btn-login:hover {
    background: #c89b3c;
    color: #1a1a2e;
}

.btn-logout {
    background: transparent;
    color: #a0a0a0;
    border: 1px solid #a0a0a0;
    padding: 8px 16px;
    border-radius: 4px;
    cursor: pointer;
    font-weight: 500;
    transition: all 0.2s;
}

.btn-logout:hover {
    border-color: #ff4757;
    color: #ff4757;
}
</style>
