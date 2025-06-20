<div class="modal-container" id="modal-key">
  <div class="modalV-content">
    <div class="modalV-header">Chọn khóa</div>
    <div class="radio-group">
      <div class="radio-item">
        <input type="radio" id="system-key" name="key-option" value="system" checked>
        <label for="system-key">Sử dụng key do hệ thống cung cấp</label>
      </div>
      <div class="radio-item">
        <input type="radio" id="personal-key" name="key-option" value="personal">
        <label for="personal-key">Sử dụng key cá nhân</label>
      </div>
      <div id="key-input-section">
        <input type="text" id="key-input" placeholder="Nhập key cá nhân" name="personal-key" />
        <button type="button" id="load-key-btn">Load Key</button>
      </div>
    </div>
    <div class="modalV-footer">
      <button id="continue-btn1" class="confirm">Tiếp tục</button>
    </div>
  </div>
</div>
