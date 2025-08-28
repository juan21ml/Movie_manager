// Movie Manager App
class MovieManager {
    constructor() {
        this.baseUrl = '/api/movies';
        this.imageBaseUrl = 'https://image.tmdb.org/t/p/w500';
        this.currentSection = 'search';
        this.favorites = [];

        this.init();
    }

    init() {
        this.setupEventListeners();
        this.loadFavorites();
        this.showSection('search');
    }

    setupEventListeners() {
        // Navigation
        document.querySelectorAll('.nav-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const section = e.currentTarget.dataset.section;
                this.showSection(section);
            });
        });

        // Search
        document.getElementById('search-btn').addEventListener('click', () => {
            this.searchMovies();
        });

        document.getElementById('search-input').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.searchMovies();
            }
        });

        // Modal
        document.querySelector('.modal-close').addEventListener('click', () => {
            this.closeModal();
        });

        document.getElementById('movie-modal').addEventListener('click', (e) => {
            if (e.target.id === 'movie-modal') {
                this.closeModal();
            }
        });

        // Modal favorite button
        document.getElementById('modal-favorite-btn').addEventListener('click', () => {
            const tmdbId = document.getElementById('modal-favorite-btn').dataset.tmdbId;
            const isInFavorites = document.getElementById('modal-favorite-btn').dataset.inFavorites === 'true';

            if (isInFavorites) {
                const movieId = document.getElementById('modal-favorite-btn').dataset.movieId;
                this.removeFromFavorites(movieId);
            } else {
                this.addToFavorites(tmdbId);
            }
        });
    }

    async showSection(section) {
        // Update navigation
        document.querySelectorAll('.nav-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        document.querySelector(`[data-section="${section}"]`).classList.add('active');

        // Update sections
        document.querySelectorAll('.section').forEach(sec => {
            sec.classList.remove('active');
        });
        document.getElementById(`${section}-section`).classList.add('active');

        this.currentSection = section;

        // Load content based on section
        switch (section) {
            case 'popular':
                await this.loadPopularMovies();
                break;
            case 'top-rated':
                await this.loadTopRatedMovies();
                break;
            case 'favorites':
                await this.loadFavorites();
                break;
        }
    }

    async searchMovies() {
        const query = document.getElementById('search-input').value.trim();
        if (!query) {
            this.showNotification('Por favor ingresa un término de búsqueda', 'warning');
            return;
        }

        this.showLoading();

        try {
            const response = await fetch(`${this.baseUrl}/search?q=${encodeURIComponent(query)}`);
            const movies = await response.json();

            this.displayMovies(movies, 'search-results');

            if (movies.length === 0) {
                this.showNoResults('search-results', 'No se encontraron películas', `No hay resultados para "${query}"`);
            }
        } catch (error) {
            console.error('Error searching movies:', error);
            this.showNotification('Error al buscar películas', 'error');
        } finally {
            this.hideLoading();
        }
    }

    async loadPopularMovies() {
        this.showLoading();

        try {
            const response = await fetch(`${this.baseUrl}/popular`);
            const movies = await response.json();

            this.displayMovies(movies, 'popular-results');
        } catch (error) {
            console.error('Error loading popular movies:', error);
            this.showNotification('Error al cargar películas populares', 'error');
        } finally {
            this.hideLoading();
        }
    }

    async loadTopRatedMovies() {
        this.showLoading();

        try {
            const response = await fetch(`${this.baseUrl}/top-rated`);
            const movies = await response.json();

            this.displayMovies(movies, 'top-rated-results');
        } catch (error) {
            console.error('Error loading top rated movies:', error);
            this.showNotification('Error al cargar películas mejor valoradas', 'error');
        } finally {
            this.hideLoading();
        }
    }

    async loadFavorites() {
        try {
            const response = await fetch(`${this.baseUrl}/favorites`);
            this.favorites = await response.json();

            this.updateFavoritesCount();

            if (this.currentSection === 'favorites') {
                if (this.favorites.length === 0) {
                    document.getElementById('favorites-results').innerHTML = '';
                    document.getElementById('no-favorites').style.display = 'block';
                } else {
                    document.getElementById('no-favorites').style.display = 'none';
                    this.displayMovies(this.favorites, 'favorites-results', true);
                }
            }
        } catch (error) {
            console.error('Error loading favorites:', error);
            this.showNotification('Error al cargar favoritas', 'error');
        }
    }

    displayMovies(movies, containerId, isFavorites = false) {
        const container = document.getElementById(containerId);
        container.innerHTML = '';

        movies.forEach(movie => {
            const movieCard = this.createMovieCard(movie, isFavorites);
            container.appendChild(movieCard);
        });
    }

    createMovieCard(movie, isFavorites = false) {
        const card = document.createElement('div');
        card.className = 'movie-card';
        card.addEventListener('click', () => this.showMovieModal(movie, isFavorites));

        const posterUrl = movie.poster_path
            ? `${this.imageBaseUrl}${movie.poster_path}`
            : 'https://via.placeholder.com/300x400?text=Sin+Imagen';

        const releaseYear = movie.release_date
            ? new Date(movie.release_date).getFullYear()
            : 'Desconocido';

        const rating = movie.vote_average
            ? movie.vote_average.toFixed(1)
            : 'N/A';

        const isInFavorites = this.isMovieInFavorites(movie.tmdb_id);

        card.innerHTML = `
            <div class="movie-poster">
                <img src="${posterUrl}" alt="${movie.title}" loading="lazy">
                <div class="movie-rating">
                    <i class="fas fa-star"></i> ${rating}
                </div>
                ${isInFavorites ? '<div class="favorite-badge"><i class="fas fa-heart"></i> Favorita</div>' : ''}
            </div>
            <div class="movie-info">
                <h3 class="movie-title">${movie.title}</h3>
                <p class="movie-overview">${movie.overview || 'Sin descripción disponible'}</p>
                <div class="movie-meta">
                    <span class="movie-year">${releaseYear}</span>
                    ${isFavorites
                        ? `<button class="btn-remove" onclick="event.stopPropagation(); movieManager.removeFromFavorites(${movie.id})">
                             <i class="fas fa-trash"></i> Quitar
                           </button>`
                        : isInFavorites
                            ? '<span class="btn-favorite added"><i class="fas fa-heart"></i> En Favoritas</span>'
                            : `<button class="btn-favorite" onclick="event.stopPropagation(); movieManager.addToFavorites(${movie.tmdb_id})">
                                 <i class="fas fa-heart"></i> Favorita
                               </button>`
                    }
                </div>
            </div>
        `;

        return card;
    }

    showMovieModal(movie, isFavorites = false) {
        const modal = document.getElementById('movie-modal');

        // Populate modal data
        document.getElementById('modal-title').textContent = movie.title;
        document.getElementById('modal-poster').src = movie.poster_path
            ? `${this.imageBaseUrl}${movie.poster_path}`
            : 'https://via.placeholder.com/300x400?text=Sin+Imagen';
        document.getElementById('modal-release').textContent = movie.release_date || 'Desconocido';
        document.getElementById('modal-rating').textContent = movie.vote_average
            ? movie.vote_average.toFixed(1)
            : 'N/A';
        document.getElementById('modal-language').textContent = movie.original_language
            ? movie.original_language.toUpperCase()
            : 'Desconocido';
        document.getElementById('modal-genres').textContent = movie.genres && movie.genres.length > 0
            ? movie.genres.join(', ')
            : 'No especificado';
        document.getElementById('modal-overview').textContent = movie.overview || 'Sin descripción disponible';

        // Setup favorite button
        const favoriteBtn = document.getElementById('modal-favorite-btn');
        const isInFavorites = this.isMovieInFavorites(movie.tmdb_id) || isFavorites;

        favoriteBtn.dataset.tmdbId = movie.tmdb_id;
        favoriteBtn.dataset.inFavorites = isInFavorites;

        if (isFavorites) {
            favoriteBtn.dataset.movieId = movie.id;
            favoriteBtn.innerHTML = '<i class="fas fa-trash"></i> Quitar de Favoritas';
            favoriteBtn.className = 'btn btn-remove';
        } else if (isInFavorites) {
            favoriteBtn.innerHTML = '<i class="fas fa-heart"></i> Ya en Favoritas';
            favoriteBtn.className = 'btn btn-favorite added';
            favoriteBtn.disabled = true;
        } else {
            favoriteBtn.innerHTML = '<i class="fas fa-heart"></i> Agregar a Favoritas';
            favoriteBtn.className = 'btn btn-favorite';
            favoriteBtn.disabled = false;
        }

        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    }

    closeModal() {
        document.getElementById('movie-modal').style.display = 'none';
        document.body.style.overflow = 'auto';
    }

    async addToFavorites(tmdbId) {
        this.showLoading();

        try {
            const response = await fetch(`${this.baseUrl}/${tmdbId}/favorite`, {
                method: 'POST'
            });

            if (response.ok) {
                this.showNotification('Película agregada a favoritas', 'success');
                await this.loadFavorites();

                // Update current view if needed
                if (this.currentSection === 'search') {
                    // Refresh search results to update buttons
                    const query = document.getElementById('search-input').value.trim();
                    if (query) {
                        await this.searchMovies();
                    }
                } else if (this.currentSection === 'popular') {
                    await this.loadPopularMovies();
                } else if (this.currentSection === 'top-rated') {
                    await this.loadTopRatedMovies();
                }

                this.closeModal();
            } else {
                this.showNotification('Error al agregar a favoritas', 'error');
            }
        } catch (error) {
            console.error('Error adding to favorites:', error);
            this.showNotification('Error al agregar a favoritas', 'error');
        } finally {
            this.hideLoading();
        }
    }

    async removeFromFavorites(movieId) {
        if (!confirm('¿Estás seguro de que quieres quitar esta película de favoritas?')) {
            return;
        }

        this.showLoading();

        try {
            const response = await fetch(`${this.baseUrl}/${movieId}/favorite`, {
                method: 'DELETE'
            });

            if (response.ok) {
                this.showNotification('Película quitada de favoritas', 'success');
                await this.loadFavorites();
                this.closeModal();
            } else {
                this.showNotification('Error al quitar de favoritas', 'error');
            }
        } catch (error) {
            console.error('Error removing from favorites:', error);
            this.showNotification('Error al quitar de favoritas', 'error');
        } finally {
            this.hideLoading();
        }
    }

    isMovieInFavorites(tmdbId) {
        return this.favorites.some(fav => fav.tmdb_id === tmdbId);
    }

    updateFavoritesCount() {
        document.getElementById('favorites-count').textContent = this.favorites.length;
    }

    showLoading() {
        document.getElementById('loading').style.display = 'flex';
    }

    hideLoading() {
        document.getElementById('loading').style.display = 'none';
    }

    showNotification(message, type = 'success') {
        const notification = document.getElementById('notification');
        const notificationText = document.getElementById('notification-text');

        notificationText.textContent = message;

        // Set color based on type
        if (type === 'error') {
            notification.style.background = '#dc3545';
        } else if (type === 'warning') {
            notification.style.background = '#ff9800';
        } else {
            notification.style.background = '#4CAF50';
        }

        notification.style.display = 'flex';

        setTimeout(() => {
            notification.style.display = 'none';
        }, 3000);
    }

    showNoResults(containerId, title, message) {
        const container = document.getElementById(containerId);
        container.innerHTML = `
            <div class="no-results">
                <i class="fas fa-search"></i>
                <h3>${title}</h3>
                <p>${message}</p>
            </div>
        `;
    }
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.movieManager = new MovieManager();
});

// Add some utility functions to window object for inline event handlers
window.addToFavorites = (tmdbId) => {
    window.movieManager.addToFavorites(tmdbId);
};

window.removeFromFavorites = (movieId) => {
    window.movieManager.removeFromFavorites(movieId);
};