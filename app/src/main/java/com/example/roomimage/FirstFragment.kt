package com.example.roomimage

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.db.model.entity.Product
import com.example.roomimage.app.ProductApplication
import com.example.roomimage.databinding.FragmentFirstBinding
import com.example.roomimage.viewmodel.ProductViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModel.ProductViewModelFactory((this@FirstFragment.requireActivity().application as ProductApplication).repository)
    }

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @SuppressLint("MissingPermission")
    fun permissionHandler(){
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Log.d(TAG,"READ_EXTERNAL_STORAGE permission is granted. showDialog to select new Image")
                    showSelectImageProduct()
                } else {
                    showMessagePermissionRequeriedForProductImage()
                    Log.d(TAG,"READ_EXTERNAL_STORAGE permission is not granted. Show current layout for explanation .")
                }
            }

    }

    fun checkAndRequestPermission(){
        if ( ActivityCompat.checkSelfPermission(
                this@FirstFragment.requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG,"Check for internet permission.")
            requestPermissionLauncher.launch(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return
        } else{
            Log.d(TAG,"READ_EXTERNAL_STORAGE permission is already granted.")

            showSelectImageProduct()
        }
    }

    private fun showSelectImageProduct() {
        // https://developer.android.com/training/basics/intents/result#launch
        // Pass in the mime type you want to let the user select
        // as the input
        getContent.launch("image/*")
    }

    //Method
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { ituri->
            Log.d(TAG,"path= ${ituri.path}")
            binding.imageView.load(ituri)
        }
        // Handle the returned Uri
        Log.d(TAG,"List of images")
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonList.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_itemFragment)
        }

        binding.buttonAdd.setOnClickListener {
            insertProductToDB()
        }

        binding.imageView.setOnClickListener {
            checkAndRequestPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun insertProductToDB() {
        val name: String = binding.nameInput.editText?.text.toString()
        val desc: String = binding.descInput.editText?.text.toString()
        val price: Float = inputValidation(binding.priceInput.editText?.text.toString())
        val url: String = "https://www.pngkit.com/png/detail/766-7660338_fruit-apple-food-imagenes-de-comida-saludable-animada.png"

        if (inputValidation(name, desc, price, url)) {
            val priceFloat = price.toFloat()
            val product: Product = Product(0, name, desc, url, 1.1F)
            viewModel.insert(product)
            Snackbar.make(binding.root, "Producto agregado satisfactoriamente", Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(binding.root, "Revise que los campos esten llenos!", Snackbar.LENGTH_LONG).show()
        }

    }

    fun inputValidation(name: String, desc: String, price: Float, url: String): Boolean {
        return name != "" && desc != "" && price > 0F && url != ""
    }

    fun inputValidation(price: String): Float {
        if (price == "" || price == null) {
            return 0F
        }
        return price.toFloat()
    }

    private fun showMessagePermissionRequeriedForProductImage() {
        TODO("Not yet implemented")
    }

    companion object{
        private const val PERMISSION_REQUEST_NOTIFICATION = 10
        private const val TAG="FirstFragment"
    }
}