#!/bin/bash
# Exit immediately if a command exits with a non-zero status
set -e
echo "========================================="
echo "   Fixing SSL & System Environment       "
echo "========================================="
# Ensure time is correct (SSL will fail if clock is wrong)
sudo timedatectl set-ntp true

# Fix permissions and clear previous failed attempts
echo "Repairing home directory permissions..."
sudo chown -R $USER:$USER $HOME 2>/dev/null || true
rm -rf "$HOME/.nvm" "$HOME/.sdkman"

echo "Updating package index and repairing certificates..."
sudo apt-get update
sudo apt-get install -y ca-certificates wget curl git zip unzip build-essential
sudo update-ca-certificates

echo "========================================="
echo "        Starting Docker Installation       "
echo "========================================="
echo "Updating package index..."
sudo apt-get update
echo "Installing prerequisite packages (curl, ca-certificates, zip, unzip)..."
# zip and unzip are added here because SDKMAN! requires them
sudo apt-get install -y ca-certificates curl zip unzip
echo "Adding Docker's official GPG key..."
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo "Adding Docker repository to Apt sources..."
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
echo "Updating package index with new repository..."
sudo apt-get update
echo "Installing Docker Engine, CLI, containerd, and plugins..."
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
echo "Enabling Docker and containerd to start on boot via systemctl..."
sudo systemctl enable docker.service
sudo systemctl enable containerd.service
echo "Starting Docker service..."
sudo systemctl start docker.service
echo "Verifying Docker installation..."
sudo docker --version
echo "========================================="
echo "    Starting Java & Maven Installation     "
echo "========================================="
echo "Downloading and installing SDKMAN!..."
# The standard curl installation is non-interactive by default
curl -s "https://get.sdkman.io" | bash

# Define the SDKMAN directory
export SDKMAN_DIR="$HOME/.sdkman"

echo "Configuring SDKMAN! to automatically answer 'yes' to prompts..."
sed -i 's/sdkman_auto_answer=false/sdkman_auto_answer=true/g' "$SDKMAN_DIR/etc/config"

echo "Loading SDKMAN! into the current shell..."
# FIX 1: Use the guard [[ -s ]] to safely source only if the file exists and is non-empty
# This handles non-interactive subshells where sdkman-init.sh may not auto-load
[[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh"

echo "Installing Java 21.0.10-tem..."
sdk install java 21.0.10-tem
echo "Installing Maven..."
sdk install maven

# FIX 2: Ensure SDKMAN is persisted in ~/.bashrc for new terminals
echo "Ensuring SDKMAN is available in future terminal sessions..."
if ! grep -q "sdkman-init.sh" ~/.bashrc; then
  echo '' >> ~/.bashrc
  echo '#THIS MUST BE AT THE END OF THE FILE FOR SDKMAN TO WORK!!!' >> ~/.bashrc
  echo 'export SDKMAN_DIR="$HOME/.sdkman"' >> ~/.bashrc
  echo '[[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh"' >> ~/.bashrc
  echo "SDKMAN entry added to .bashrc"
else
  echo "SDKMAN entry already present in .bashrc, skipping..."
fi

echo "========================================="
echo "   Starting Node.js & Angular Setup      "
echo "========================================="
echo "Installing prerequisites..."
sudo apt-get update
sudo apt-get install --reinstall ca-certificates -y
sudo apt-get install -y curl git
sudo update-ca-certificates

export NVM_DIR="$HOME/.nvm"
NVM_VERSION="v0.40.1"

echo "Cloning NVM repository (version $NVM_VERSION)..."
if [ -d "$NVM_DIR" ]; then
    echo "  $NVM_DIR already exists — removing for a clean install."
    rm -rf "$NVM_DIR"
fi

if ! git clone --depth 1 -b "$NVM_VERSION" https://github.com/nvm-sh/nvm.git "$NVM_DIR"; then
    echo "ERROR: Failed to clone NVM repository."
    echo "Check your network connection to github.com:"
    echo "  curl -v https://github.com 2>&1 | head -20"
    exit 1
fi

echo "Running NVM install script..."
(cd "$NVM_DIR" && bash install.sh)

echo "Loading NVM into current shell..."
if [ ! -s "$NVM_DIR/nvm.sh" ]; then
    echo "ERROR: nvm.sh not found at $NVM_DIR/nvm.sh after install."
    ls -la "$NVM_DIR" 2>/dev/null
    exit 1
fi
\. "$NVM_DIR/nvm.sh"

if ! command -v nvm >/dev/null 2>&1; then
    echo "ERROR: nvm.sh was sourced but 'nvm' command is still not available"
    exit 1
fi

echo "NVM loaded: $(nvm --version)"
echo "Installing Node.js LTS and Angular CLI..."
nvm install --lts
nvm use --lts
npm install -g @angular/cli

echo "Verifying installation..."
echo "  Node: $(node --version)"
echo "  npm:  $(npm --version)"
echo "  ng:   $(ng version --skip-confirmation 2>/dev/null | grep -E '^Angular CLI' || echo 'Angular CLI installed')"

echo "========================================="
echo "         Final Verification              "
echo "========================================="
echo "Node: $(node -v)"
echo "NPM:  $(npm -version)"
echo "Java: $(java -version 2>&1 | head -n 1)"
echo "NG:   $(ng version | grep 'Angular CLI' | head -n 1)"
echo "========================================="
echo "Setup complete! Close this terminal and open a new one."
echo "========================================="

echo "========================================="
echo "        All Installations Complete!        "
echo "========================================="
echo "Important Next Steps:"
echo "1. To run Docker commands without 'sudo', run: sudo usermod -aG docker \$USER"
echo "2. Log out and log back in (or restart your machine) for the Docker group changes to take effect."
echo "3. SDKMAN! has updated your .bashrc/.zshrc. If Java or Maven aren't recognized in a new terminal window, run: source ~/.bashrc"
echo "========================================="
