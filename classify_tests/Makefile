classify_tests:
	@echo "Downloading required packages..."
	pip install -r requirements.txt
	@echo "Downloading required packages... Done\n"
	@echo "Creating output directory..."
	mkdir -p output_json_files
	@echo "Creating output directory... Done\n"
	@echo "Classifying test symptoms..."
	python3 classify_test.py
	@echo "Classifying test symptoms Done\n"