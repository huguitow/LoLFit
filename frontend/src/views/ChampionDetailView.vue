<script setup>
    import { ref, onMounted } from 'vue'
    import { useRoute } from 'vue-router'
    import { useAuthStore } from '../stores/auth'

    const route = useRoute()
    const authStore = useAuthStore()

    const champion = ref(null)
    const loading = ref(true)
    const error = ref(null)

    onMounted(() => {
        fetchChampionDetails()
    })

    const fetchChampionDetails = async () => {
        try {
            const championId = route.params.id
            const response = await fetch(`http://localhost:8080/api/skins/champions/${championId}`, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token
                }
            })

            if (!response.ok) {
                throw new Error('Champion non trouvé')
            }

            const data = await response.json()
            champion.value = data
        } catch (err) {
            error.value = err.message
            console.error('Erreur lors de la récupération du champion :', err)
        } finally {
            loading.value = false
        }
    }

    const addToWardrobe = async (skinId) => {
        try {
            const response = await fetch('http://localhost:8080/api/wardrobe', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ skinId: skinId })
            })

            if (response.ok) {
                alert('Skin ajouté à votre garde-robe !')
            } else {
                alert('Erreur lors de l\'ajout du skin')
            }
        } catch (err) {
            console.error('Erreur lors de l\'ajout à la garde-robe :', err)
            alert('Erreur lors de l\'ajout du skin')
        }
    }
</script>

<template>
    <div class="champion-detail">
        <div v-if="loading" class="loading">Chargement...</div>

        <div v-else-if="error" class="error">{{ error }}</div>

        <div v-else-if="champion">
            <h1>{{ champion.name }}</h1>
            <p>{{ champion.skins?.length || 0 }} skins disponibles</p>

            <div class="skins-grid">
                <div v-for="skin in champion.skins" :key="skin.id" class="skin-card">
                    <img :src="skin.imageUrl" :alt="skin.name">
                    <p class="skin-name">{{ skin.name }}</p>
                    <button @click="addToWardrobe(skin.id)" class="add-btn">
                        Ajouter à ma garde-robe
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .champion-detail {
        padding: 20px;
    }

    .loading, .error {
        text-align: center;
        padding: 40px;
        font-size: 1.2em;
    }

    .error {
        color: red;
    }

    .skins-grid {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        margin-top: 20px;
    }

    .skin-card {
        width: 250px;
        border: 1px solid #ccc;
        border-radius: 8px;
        padding: 10px;
        text-align: center;
    }

    .skin-card img {
        width: 100%;
        border-radius: 4px;
    }

    .skin-name {
        font-weight: bold;
        margin: 10px 0;
    }

    .add-btn {
        background-color: #4CAF50;
        color: white;
        border: none;
        padding: 10px 15px;
        border-radius: 4px;
        cursor: pointer;
        width: 100%;
    }

    .add-btn:hover {
        background-color: #45a049;
    }
</style>
