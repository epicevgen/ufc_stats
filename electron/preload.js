const { contextBridge, ipcRenderer } = require('electron');

// Безопасный API для взаимодействия с Electron
contextBridge.exposeInMainWorld('electronAPI', {
  // Получение информации о приложении
  getAppInfo: () => ipcRenderer.invoke('get-app-info'),
  
  // Создание резервной копии
  createBackup: () => ipcRenderer.invoke('create-backup'),
  
  // Платформа
  platform: process.platform,
  
  // Версия
  version: process.versions.electron
});

// Логирование для отладки
console.log('Preload script загружен');
