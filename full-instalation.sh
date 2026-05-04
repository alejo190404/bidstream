#!/bin/bash
# Exit immediately if a command exits with a non-zero status
set -e
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

echo "Verifying Java and Maven installations..."
java -version
mvn -version
echo "========================================="
echo "        All Installations Complete!        "
echo "========================================="
echo "Important Next Steps:"
echo "1. To run Docker commands without 'sudo', run: sudo usermod -aG docker \$USER"
echo "2. Log out and log back in (or restart your machine) for the Docker group changes to take effect."
echo "3. SDKMAN! has updated your .bashrc/.zshrc. If Java or Maven aren't recognized in a new terminal window, run: source ~/.bashrc"
echo "========================================="
