document.getElementById('search').addEventListener('click', performSearch);
document.getElementById('postcode').addEventListener('keydown', function(e) {
    if (e.key === 'Enter') performSearch();
});
async function performSearch() {
    const postcode = document.getElementById('postcode').value.trim();
    const error = document.getElementById('error');
    const results = document.getElementById('results');

    error.textContent = '';
    results.innerHTML = '';

    if (!postcode) {
        error.textContent = 'Please enter a postcode.';
        return;
    }
    results.textContent = 'Loading...';
    try {
        const res = await fetch(`/restaurants/${encodeURIComponent(postcode)}`);
        if (!res.ok) {
            error.textContent = 'Failed to fetch restaurants. Please try again.';
            return;
        }
        const restaurants = await res.json();

        if (restaurants.length === 0) {
            results.textContent = 'No restaurants found.';
            return;
        }

        let html = `<p>First ${restaurants.length} restaurants found in ${postcode.toUpperCase()} are displayed</p>`;
        html += '<table><thead><tr><th>No.</th><th>Name</th><th>Rating</th><th>Cuisines</th><th>Address</th></tr></thead><tbody>';

        for (let i = 0; i < restaurants.length; i++) {
            const r = restaurants[i];
            const cuisines = r.cuisines.join(', ');
            html += `<tr>
            <td>${i + 1}</td>
            <td>${r.name}</td>
            <td>${r.rating}</td>
            <td>${cuisines}</td>
            <td>${r.address}</td>
        </tr>`;
        }
        html += '</tbody></table>';
        results.innerHTML = html;
    } catch (e) {
        error.textContent = 'Network error. Please check your connection.';
        results.textContent = '';
    }
}
