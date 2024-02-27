let userCount = 0;

function addUser() {
    const usersContainer = document.getElementById('usersContainer');
    const userDiv = document.createElement('div');
    userDiv.id = `user${userCount}`;
    userDiv.innerHTML = `
        <h3>Usuário ${userCount + 1}</h3>
        <label for="userName${userCount}">Nome:</label>
        <input type="text" id="userName${userCount}" name="userName${userCount}"><br><br>
        <div id="itemsContainer${userCount}"></div>
        <button type="button" onclick="addItem(${userCount})">Adicionar Item</button><br><br>
    `;
    usersContainer.appendChild(userDiv);
    addItem(userCount); // Adiciona o primeiro item automaticamente
    userCount++;
}

function addItem(userIndex) {
    const itemsContainer = document.getElementById(`itemsContainer${userIndex}`);
    const itemCount = itemsContainer.childElementCount;
    const itemDiv = document.createElement('div');
    itemDiv.innerHTML = `
        <h4>Item ${itemCount + 1}</h4>
        <label for="itemName${userIndex}_${itemCount}">Nome do Item:</label>
        <input type="text" id="itemName${userIndex}_${itemCount}" name="itemName${userIndex}_${itemCount}"><br><br>
        
        <label for="itemPrice${userIndex}_${itemCount}">Preço:</label>
        <input type="number" id="itemPrice${userIndex}_${itemCount}" name="itemPrice${userIndex}_${itemCount}" step="0.01"><br><br>
        
        <label for="itemQuantity${userIndex}_${itemCount}">Quantidade:</label>
        <input type="number" id="itemQuantity${userIndex}_${itemCount}" name="itemQuantity${userIndex}_${itemCount}"><br><br>
    `;
    itemsContainer.appendChild(itemDiv);
}

document.getElementById('addUser').addEventListener('click', addUser);

document.getElementById('orderForm').addEventListener('submit', function(event) {
  event.preventDefault();

  // Coleta os dados do pedido
  const orderData = {
      discount: parseFloat(document.getElementById('discount').value),
      deliveryFee: parseFloat(document.getElementById('deliveryFee').value),
      discountPercentage: parseFloat(document.getElementById('discountPercentage').value),
      serviceChargePercentage: parseFloat(document.getElementById('serviceChargePercentage').value),
      users: []
  };

  // Coleta os dados dos usuários e itens
  for (let i = 0; i < userCount; i++) {
      const userName = document.getElementById(`userName${i}`).value;
      const itemsContainer = document.getElementById(`itemsContainer${i}`);
      const items = [];

      for (let j = 0; j < itemsContainer.childElementCount; j++) {
          const itemName = document.getElementById(`itemName${i}_${j}`).value;
          const itemPrice = parseFloat(document.getElementById(`itemPrice${i}_${j}`).value);
          const itemQuantity = parseInt(document.getElementById(`itemQuantity${i}_${j}`).value);

          if (itemName && itemPrice && itemQuantity) {
              items.push({ name: itemName, price: itemPrice, quantity: itemQuantity });
          }
      }

      if (userName && items.length > 0) {
          orderData.users.push({ name: userName, items: items });
      }
  }

  console.log(orderData); // Para depuraç
  fetch('http://localhost:8080/orders', {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify(orderData),
  })
  .then(response => response.json())
  .then(data => {
      document.getElementById('response').textContent = JSON.stringify(data, null, 2);
  })
  .catch((error) => {
    console.error('Erro:', error);
    document.getElementById('response').textContent = 'Falha ao enviar pedido: ' + error.message;
});
});
