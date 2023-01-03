package com.androidproject.app.appcomponents.activities.admin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Product
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ModifyProductFragment : Fragment() {

    private lateinit var previewImage:ImageView
    private lateinit var name:TextInputEditText
    private lateinit var price:TextInputEditText
    private lateinit var getImage:Button
    private lateinit var modify:Button
    private lateinit var delete:Button

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { Glide.with(requireContext()).load(uri).into(previewImage) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_modify_product, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val product: Product = requireArguments().getSerializable("EXTRA_PRODUCT") as Product

        previewImage = view.findViewById(R.id.preview_product_admin_modify)
        name = view.findViewById(R.id.prod_name_admin_input_modify)
        price = view.findViewById(R.id.prod_price_admin_input_modify)
        getImage = view.findViewById(R.id.get_image_admin_modify)
        modify = view.findViewById(R.id.modify_product_admin)
        delete = view.findViewById(R.id.delete_product_admin)

        val imageBytes = Base64.decode(product.image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        previewImage.setImageBitmap(image)

        name.setText(product.name)

        price.setText(product.price)

        getImage.setOnClickListener{
            selectImageFromGallery()
        }

        delete.setOnClickListener{

            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            val apiInterface = ApiInterface.create()
            apiInterface.deleteProducts(product).enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    val result= response.body()
                    if (result != null) {
                        Toast.makeText(requireContext(), "Product Deleted!" , Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    }else{
                        Toast.makeText(requireContext(), "Error ", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    Toast.makeText(requireContext(), "Error Server", Toast.LENGTH_SHORT).show()
                }

            })

        }

        modify.setOnClickListener {

            if(previewImage.drawable != null || name.text.toString().isNotEmpty()) {
                try {
                    price.text.toString().toFloat()
                    val apiInterface = ApiInterface.create()

                    val bitmap = previewImage.drawable.toBitmap()
                    val newProduct = Product(
                        id = product.id,
                        name = name.text.toString(),
                        price = price.text.toString(),
                        image = bitMapToString(bitmap)
                    )

                    requireActivity().window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )

                    apiInterface.modifyProducts(newProduct).enqueue(object : Callback<Product> {
                        override fun onResponse(call: Call<Product>, response: Response<Product>) {
                            requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            val result = response.body()
                            if (result != null) {
                                Toast.makeText(
                                    requireContext(),
                                    "Product Added " + result.id!!,
                                    Toast.LENGTH_SHORT
                                ).show()
                                previewImage.setImageResource(android.R.color.transparent)
                                name.setText("")
                                price.setText("")
                            } else {
                                Toast.makeText(requireContext(), "Error ", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }

                        override fun onFailure(call: Call<Product>, t: Throwable) {
                            requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            Toast.makeText(requireContext(), "Error Server", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Input Error", Toast.LENGTH_SHORT).show()
                }
            }else Toast.makeText(requireContext(), "Input Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}